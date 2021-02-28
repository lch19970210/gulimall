package com.hangzhou.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author linchenghui
 * @Date 2021/2/28
 */
@Data
public class SkuReductionTo {
    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
