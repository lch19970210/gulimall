package com.hangzhou.gulimall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * 封装页面传递过来的查询条件
 * @Author linchenghui
 * @Date 2021/4/8
 */
@Data
public class SearchParam {
    /**
     * 全文匹配关键字
     */
    private String keyword;
    /**
     * 三级分类id
     */
    private Long catalog3Id;
    /**
     * 排序条件
     */
    private String sort;
    /**
     * 仅显示有货
     */
    private Integer hasStock;
    /**
     * 价格区间
     */
    private String skuPrice;
    /**
     * 品牌id 可以多选
     */
    private List<Long> brandId;
    /**
     * 按照属性进行筛选
     * 1_苹果:安卓
     * 2_5寸:6寸
     */
    private List<String> attrs;
    /**
     * 页码
     */
    private Integer pageNum = 1;
    /**
     * 原生所有查询属性
     */
    private String _queryString;
}
