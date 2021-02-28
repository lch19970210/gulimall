package com.hangzhou.common.to;

import lombok.Data;

import javax.annotation.security.DenyAll;
import java.math.BigDecimal;

/**
 * @Author linchenghui
 * @Date 2021/2/28
 */
@Data
public class SpuBoundTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
