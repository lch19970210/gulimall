package com.hangzhou.gulimall.search.controller;

import com.hangzhou.gulimall.search.service.MallSearchService;
import com.hangzhou.gulimall.search.vo.SearchParam;
import com.hangzhou.gulimall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author linchenghui
 * @Date 2021/4/7
 */
@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    /**
     * 将页面传来的查询请求参数封装成指定对象
     * @param param 查询请求参数
     * @return  对象
     */
    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model){
        // 去es中检索商品
        SearchResult result = mallSearchService.search(param);
        model.addAttribute("result",result);
        return "list";
    }
}
