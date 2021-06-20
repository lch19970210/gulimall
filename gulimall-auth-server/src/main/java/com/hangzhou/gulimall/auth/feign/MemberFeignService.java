package com.hangzhou.gulimall.auth.feign;

import com.hangzhou.common.utils.R;
import com.hangzhou.gulimall.auth.vo.UserLoggingVo;
import com.hangzhou.gulimall.auth.vo.UserRegistVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author linchenghui
 * @Date 2021/6/16
 */
@FeignClient(value = "gulimall-member")
public interface MemberFeignService {
    @PostMapping("/member/member/regist")
    R regist(@RequestBody UserRegistVo vo);

    @PostMapping("/member/member/login")
    public R login(@RequestBody UserLoggingVo vo);
}
