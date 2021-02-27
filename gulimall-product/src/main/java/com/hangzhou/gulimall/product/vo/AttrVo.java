package com.hangzhou.gulimall.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.hangzhou.gulimall.product.entity.AttrEntity;
import lombok.Data;

/**
 * @Author linchenghui
 * @Date 2021/2/27
 */
@Data
public class AttrVo extends AttrEntity{
    /**
     * 分组id
     */
    private Long attrGroupId;
}
