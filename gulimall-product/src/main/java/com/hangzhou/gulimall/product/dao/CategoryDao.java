package com.hangzhou.gulimall.product.dao;

import com.hangzhou.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author linchenghui
 * @email linchenghui@gmail.com
 * @date 2021-02-10 10:48:44
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
