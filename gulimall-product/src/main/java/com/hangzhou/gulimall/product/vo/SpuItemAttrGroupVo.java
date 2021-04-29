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
public class SpuItemAttrGroupVo {
    private String groupName;
    private List<SpuBaseAttrVo> attrs;
}
