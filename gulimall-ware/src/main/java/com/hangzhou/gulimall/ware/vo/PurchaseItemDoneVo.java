package com.hangzhou.gulimall.ware.vo;

import lombok.Data;

/**
 * @Author linchenghui
 * @Date 2021/2/28
 */
@Data
public class PurchaseItemDoneVo {
    private Long itemId;
    private Integer status;
    private String reason;
}
