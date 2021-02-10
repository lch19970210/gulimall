package com.hangzhou.gulimall.member.dao;

import com.hangzhou.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author linchenghui
 * @email linchenghui@gmail.com
 * @date 2021-02-10 14:16:08
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
