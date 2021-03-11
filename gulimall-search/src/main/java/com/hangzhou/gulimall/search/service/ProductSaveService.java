package com.hangzhou.gulimall.search.service;

import com.hangzhou.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @Author linchenghui
 * @Date 2021/3/11
 */
public interface ProductSaveService {
    /**
     * 实现商品上架功能
     * @param skuEsModels 满足Es数据模型的json数据
     */
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
