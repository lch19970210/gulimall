package com.hangzhou.gulimall.order.dao;

import com.hangzhou.gulimall.order.entity.MqMessageEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author linchenghui
 * @email linchenghui@gmail.com
 * @date 2021-02-10 14:23:46
 */
@Mapper
public interface MqMessageDao extends BaseMapper<MqMessageEntity> {
	
}
