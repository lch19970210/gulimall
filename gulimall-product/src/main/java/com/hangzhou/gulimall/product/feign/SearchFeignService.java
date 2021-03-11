package com.hangzhou.gulimall.product.feign;

import com.hangzhou.common.to.es.SkuEsModel;
import com.hangzhou.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author linchenghui
 * @Date 2021/3/11
 */
@FeignClient("gulimall-search")
public interface SearchFeignService {

    @PostMapping("/search/save/product")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
