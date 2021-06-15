package com.hangzhou.gulimall.auth.controller;

import com.hangzhou.common.constant.AuthServerConstant;
import com.hangzhou.common.exception.BizCodeEnum;
import com.hangzhou.common.utils.R;

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
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone, code, 1, TimeUnit.MINUTES);
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

        // 校验验证码
        String code = vo.getCode();
        redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        if (!StringUtils.isBlank(code)) {
            // 删除验证码
            redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
            // 调用远程服务进行注册
        } else {
            errors.putIfAbsent("code", "短信验证码错误");
            return "redirect:http://127.0.0.1:20000/reg.html";
        }
        return "redirect:http://127.0.0.1:20000/login.html";

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
