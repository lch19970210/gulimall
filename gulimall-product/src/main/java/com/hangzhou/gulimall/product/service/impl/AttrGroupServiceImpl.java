package com.hangzhou.gulimall.product.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hangzhou.common.utils.PageUtils;
import com.hangzhou.common.utils.Query;

import com.hangzhou.gulimall.product.dao.AttrGroupDao;
import com.hangzhou.gulimall.product.entity.AttrGroupEntity;
import com.hangzhou.gulimall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        // 过滤条件
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((obj) -> {
                obj.eq("attr_group_id",key).or().like("attr_group_name",key);
            });
        }
        if("0".equals(catelogId)){
            IPage<AttrGroupEntity> page = this.page(
                    // 查询对象
                    new Query<AttrGroupEntity>().getPage(params),
                    // 过滤条件
                    wrapper
            );
            return new PageUtils(page);
        }else {
            wrapper.eq("catelog_id",catelogId);
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);
        }
    }

}