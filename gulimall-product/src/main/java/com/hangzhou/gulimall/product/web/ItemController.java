package com.hangzhou.gulimall.product.web;

import com.hangzhou.gulimall.product.service.SkuInfoService;
import com.hangzhou.gulimall.product.vo.SkuItemVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;

/**
 * @Author linchenghui
 * @Date 2021/4/24
 */
@Controller
public class ItemController {

    @Resource
    SkuInfoService skuInfoService;

    @GetMapping({"/item.html"})
    public String itemPage(Model model){
        // 试图解析器会拼接前缀和后缀
        return "item";
    }

    /**
     * 查询当前sku详情
     * @param skuId skuId
     * @return sku详情
     */
    @GetMapping("{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId){
        SkuItemVo vo = skuInfoService.item(skuId);
        return "item";
    }
}
