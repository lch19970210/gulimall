package com.hangzhou.gulimall.product.vo;

import lombok.Data;

/**
 * @Author linchenghui
 * @Date 2021/2/27
 */
@Data
public class AttrRespVo extends AttrVo{
    /**
     * 所属分类名字
     */
    private String catelogName;
    /**
     * 所属分组名字
     */
    private String groupName;
    /**
     * 分类路径
     */
    private Long[] catelogPath;
}
