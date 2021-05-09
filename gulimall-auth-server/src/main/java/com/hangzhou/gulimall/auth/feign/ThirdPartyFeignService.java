package com.hangzhou.gulimall.auth.feign;

import com.hangzhou.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用第三方服务
 * @Author linchenghui
 * @Date 2021/5/9
 */
@FeignClient("gulimall-third-party")
public interface ThirdPartyFeignService {
    @GetMapping("/sms/sendCode")
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);
}
