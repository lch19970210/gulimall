package com.hangzhou.gulimall.product.feign;

import com.hangzhou.common.to.SkuReductionTo;
import com.hangzhou.common.to.SpuBoundTo;
import com.hangzhou.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author linchenghui
 * @Date 2021/2/28
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    /**
     * feign远程调用接口时,@RequestBody注解会将对象转为json,然后找到对应服务对应请求,将上一步转的json放在请求体位置当中,如果接口中的参数也有@RequestBody就能封装请求体的数据为对象(只要参数一致)
     * @param spuBoundTo 微服务之间调用的对象为To
     * @return R
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveInfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
