package com.hangzhou.gulimall.product.vo;

import com.hangzhou.gulimall.product.entity.SkuImagesEntity;
import com.hangzhou.gulimall.product.entity.SkuInfoEntity;
import com.hangzhou.gulimall.product.entity.SpuInfoDescEntity;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author linchenghui
 * @Date 2021/4/24
 */
@Data
public class SkuItemVo {
    /**
     * sku基本信息的获取:如标题
     */
    SkuInfoEntity info;

    boolean hasStock = true;

    /**
     * sku的图片信息
     */
    List<SkuImagesEntity> images;

    /**
     * 获取spu的销售属性组合。每个attrName对应一个value-list
     */
    List<SkuItemSaleAttrVo> saleAttr;

    /**
     * 获取spu的介绍
     */
    SpuInfoDescEntity desc;

    private List<SpuItemAttrGroupVo> groupAttrs;

}
