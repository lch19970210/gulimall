package com.hangzhou.gulimall.product.web;

import com.hangzhou.gulimall.product.entity.CategoryEntity;
import com.hangzhou.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Author linchenghui
 * @Date 2021/3/15
 */
@Controller
public class IndexController {
    @Autowired
    CategoryService categoryService;

    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){
        // 查出所有的一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categorys();

        model.addAttribute("categorys",categoryEntities);
        // 试图解析器会拼接前缀和后缀
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Object getCatalogJson(){
        Map<String,Object> map = categoryService.getCatalogJson();
        return map;
    }
}
