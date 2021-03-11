package com.hangzhou.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hangzhou.common.utils.PageUtils;
import com.hangzhou.gulimall.product.entity.AttrEntity;
import com.hangzhou.gulimall.product.vo.AttrGroupRelationVo;
import com.hangzhou.gulimall.product.vo.AttrRespVo;
import com.hangzhou.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author linchenghui
 * @email linchenghui@gmail.com
 * @date 2021-02-10 10:48:44
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, String attrType, Long catelogId);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] vos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    /**
     * 在指定的所有属性集合里挑出检索属性
     * @param attrIds 属性id集合
     * @return 检索属性
     */
    List<Long> selectSearchAttrIds(List<Long> attrIds);
}

