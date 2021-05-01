package com.hangzhou.gulimall.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author linchenghui
 * @Date 2021/5/1
 */
@Controller
public class LoginController {
    @GetMapping("/login.html")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/reg.html")
    public String regPage(){
        return "reg";
    }
}
