package com.hangzhou.gulimall.coupon.dao;

import com.hangzhou.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author linchenghui
 * @email linchenghui@gmail.com
 * @date 2021-02-10 14:07:30
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
