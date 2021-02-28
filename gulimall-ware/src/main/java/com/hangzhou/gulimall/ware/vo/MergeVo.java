package com.hangzhou.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author linchenghui
 * @Date 2021/2/28
 */
@Data
public class MergeVo {
    private Long purchaseId;
    private List<Long> items;
}
