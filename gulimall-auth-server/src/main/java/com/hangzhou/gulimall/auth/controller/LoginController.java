package com.hangzhou.gulimall.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.hangzhou.common.constant.AuthServerConstant;
import com.hangzhou.common.exception.BizCodeEnum;
import com.hangzhou.common.utils.R;

import com.hangzhou.gulimall.auth.feign.MemberFeignService;
import com.hangzhou.gulimall.auth.feign.ThirdPartyFeignService;
import com.hangzhou.gulimall.auth.vo.UserRegistVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author linchenghui
 * @Date 2021/5/1
 */
@Controller
public class LoginController {

    @Autowired
    ThirdPartyFeignService thirdPartyFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    MemberFeignService memberFeignServicel;

    /**
     * 发送请求调用发送短信服务
     * @param phone 手机号
     * @return R.ok()
     */
    @RequestMapping("/sms/sendCode")
    @ResponseBody
    public R sendCode(@RequestParam("phone") String phone) {
        // 接口防刷
        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);

        // 如果不为空，返回错误信息
        if(null != redisCode && redisCode.length() > 0){
            long CuuTime = Long.parseLong(redisCode.split("_")[1]);
            if(System.currentTimeMillis() - CuuTime < 60 * 1000){ // 60s
                return R.error(BizCodeEnum.SMS_CODE_EXCEPTION.getCode(), BizCodeEnum.SMS_CODE_EXCEPTION.getMsg());
            }
        }

        // 发送短信校验
        String code = UUID.randomUUID().toString().substring(0,5);
        String substring = code + "_" + System.currentTimeMillis();
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone, substring, 1, TimeUnit.MINUTES);
        thirdPartyFeignService.sendCode(phone, code);
        return R.ok();
    }

    @PostMapping("/register")
    public String register(@Valid UserRegistVo vo, BindingResult result, Model model, RedirectAttributes attributes) {
        Map<String, String> errors = new HashMap<>();
        if (result.hasErrors()){
            //1.1 如果校验不通过，则封装校验结果
            result.getFieldErrors().forEach(item->{
                // 获取错误的属性名和错误信息
                errors.put(item.getField(), item.getDefaultMessage());
                //1.2 将错误信息封装到session中
                attributes.addFlashAttribute("errors", errors);
            });
            //1.2 重定向到注册页
            return "redirect:http://127.0.0.1:20000/reg.html";
        }

        // 校验验证码(没有接入短信发送接口所以判断用户是否点击过发送短信即可)
        String smsCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        if (!StringUtils.isBlank(smsCode)) {
            // 删除验证码
            redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
            // 调用远程服务进行注册
            R r = memberFeignServicel.regist(vo);
            if (r.getCode() == 0) {
                return "redirect:http://127.0.0.1:20000/login.html";
            } else {
                errors.put("msg", "注册失败,请联系管理员");
                attributes.addFlashAttribute("errors", errors);
                return "redirect:http://127.0.0.1:20000/reg.html";
            }
        } else {
            errors.putIfAbsent("code", "短信验证码错误");
            return "redirect:http://127.0.0.1:20000/reg.html";
        }

    }

    @GetMapping({"/login","/"})
    public String loginPage(){
        return "login";
    }

    @GetMapping({"/reg"})
    public String regPage(){
        return "reg";
    }
}
