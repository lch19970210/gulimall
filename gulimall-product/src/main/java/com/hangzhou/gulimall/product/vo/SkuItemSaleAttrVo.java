package com.hangzhou.gulimall.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author linchenghui
 * @Date 2021/4/29
 */
@Data
@ToString
public class SkuItemSaleAttrVo {
    private Long attrId;
    private String attrName;

    /**
     * AttrValueWithSkuIdVo两个属性 attrValue、skuIds
     */
        private List<AttrValueWithSkuIdVo> attrValues;
//    private List<String> attrValues;
}
