package com.hangzhou.gulimall.auth.controller;

import com.hangzhou.common.constant.AuthServerConstant;
import com.hangzhou.common.exception.BizCodeEnum;
import com.hangzhou.common.utils.R;

import com.hangzhou.gulimall.auth.feign.ThirdPartyFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    @GetMapping({"/login.html","/login","/"})
    public String loginPage(){
        return "login";
    }

    @GetMapping({"/reg.html","/reg"})
    public String regPage(){
        return "reg";
    }
}
