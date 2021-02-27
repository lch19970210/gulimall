package com.hangzhou.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hangzhou.common.utils.PageUtils;
import com.hangzhou.gulimall.product.entity.BrandEntity;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

/**
 * 品牌
 *
 * @author linchenghui
 * @email linchenghui@gmail.com
 * @date 2021-02-10 10:48:44
 */
@Validated
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDetail(BrandEntity brand);
}

