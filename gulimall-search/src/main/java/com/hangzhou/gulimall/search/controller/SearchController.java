package com.hangzhou.gulimall.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author linchenghui
 * @Date 2021/4/7
 */
@Controller
public class SearchController {
    @GetMapping("/list.html")
    public String listPage(){
        return "list";
    }
}
