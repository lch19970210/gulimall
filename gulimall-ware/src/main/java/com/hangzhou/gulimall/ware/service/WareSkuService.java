package com.hangzhou.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hangzhou.common.utils.PageUtils;
import com.hangzhou.gulimall.ware.entity.WareSkuEntity;
import com.hangzhou.gulimall.ware.vo.SkuHasStockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author linchenghui
 * @email linchenghui@gmail.com
 * @date 2021-02-10 14:29:23
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds);
}

