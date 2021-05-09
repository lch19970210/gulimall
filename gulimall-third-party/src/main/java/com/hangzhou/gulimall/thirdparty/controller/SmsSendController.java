package com.hangzhou.gulimall.thirdparty.controller;

import com.hangzhou.common.utils.R;
import com.hangzhou.gulimall.thirdparty.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author linchenghui
 * @Date 2021/5/9
 */
@RestController
@RequestMapping("/sms")
public class SmsSendController {

    @Autowired
    SmsComponent smsComponent;

    /**
     * 提供给别的服务调用短信功能
     * @param phone 手机号
     * @param code 自定义验证码
     * @return R.ok()
     */
    @GetMapping("/sendCode")
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code){
//        smsComponent.sengSmsCode(phone,code);
        return R.ok();
    }
}
