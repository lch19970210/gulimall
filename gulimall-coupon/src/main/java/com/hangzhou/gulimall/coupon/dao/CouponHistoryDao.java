package com.hangzhou.gulimall.coupon.dao;

import com.hangzhou.gulimall.coupon.entity.CouponHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 * 
 * @author linchenghui
 * @email linchenghui@gmail.com
 * @date 2021-02-10 14:07:30
 */
@Mapper
public interface CouponHistoryDao extends BaseMapper<CouponHistoryEntity> {
	
}
