package com.hangzhou.gulimall.search.controller;

import com.hangzhou.common.exception.BizCodeEnum;
import com.hangzhou.common.to.es.SkuEsModel;
import com.hangzhou.common.utils.R;
import com.hangzhou.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author linchenghui
 * @Date 2021/3/11
 */
@RestController
@RequestMapping("/search/save")
@Slf4j
public class ElasticSaveController {

    @Autowired
    ProductSaveService productSaveService;

    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels){
        boolean b = false;
        try{
            b = productSaveService.productStatusUp(skuEsModels);
        }catch (Exception e){
            log.error("ElasticSaveController 商品上架错误:{}",e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
        if(b){
            return R.ok();
        }else {
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }
}
