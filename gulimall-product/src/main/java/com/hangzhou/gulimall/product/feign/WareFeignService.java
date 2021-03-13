package com.hangzhou.gulimall.product.feign;

import com.hangzhou.common.to.SkuHasStockVo;
import com.hangzhou.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author linchenghui
 * @Date 2021/3/11
 */
@FeignClient("gulimall-ware")
public interface WareFeignService {

    /**
     * 查看是否有库存
     * @param skuIds
     * @return
     */
    @PostMapping("/ware/waresku/hasstock")
    R getSkusHasStock(@RequestBody List<Long> skuIds);

}
