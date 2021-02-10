package com.hangzhou.gulimall.order.dao;

import com.hangzhou.gulimall.order.entity.RefundInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退款信息
 * 
 * @author linchenghui
 * @email linchenghui@gmail.com
 * @date 2021-02-10 14:23:46
 */
@Mapper
public interface RefundInfoDao extends BaseMapper<RefundInfoEntity> {
	
}
