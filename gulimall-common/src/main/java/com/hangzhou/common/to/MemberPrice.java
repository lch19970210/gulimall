package com.hangzhou.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author linchenghui
 * @Date 2021/2/28
 */
@Data
public class MemberPrice {

    private Long id;
    private String name;
    private BigDecimal price;

}
