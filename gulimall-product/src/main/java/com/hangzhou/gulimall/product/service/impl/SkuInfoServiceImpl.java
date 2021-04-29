package com.hangzhou.gulimall.product.service.impl;

import com.hangzhou.gulimall.product.entity.SkuImagesEntity;
import com.hangzhou.gulimall.product.entity.SpuInfoDescEntity;
import com.hangzhou.gulimall.product.service.AttrGroupService;
import com.hangzhou.gulimall.product.service.SkuImagesService;
import com.hangzhou.gulimall.product.service.SpuInfoDescService;
import com.hangzhou.gulimall.product.vo.Images;
import com.hangzhou.gulimall.product.vo.SkuItemVo;
import com.hangzhou.gulimall.product.vo.SpuItemAttrGroupVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hangzhou.common.utils.PageUtils;
import com.hangzhou.common.utils.Query;

import com.hangzhou.gulimall.product.dao.SkuInfoDao;
import com.hangzhou.gulimall.product.entity.SkuInfoEntity;
import com.hangzhou.gulimall.product.service.SkuInfoService;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    SkuImagesService imagesService;
    @Autowired
    SpuInfoDescService spuInfoDescService;
    @Autowired
    AttrGroupService attrGroupService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if(StringUtils.isNotEmpty(key)){
            queryWrapper.and(wrapper ->{
                wrapper.eq("sku_id",key).or().like("sku_name",key);
            });
        }

        String catelogId = (String) params.get("catelogId");
        if(StringUtils.isNotEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)){
            queryWrapper.eq("catalog_id",catelogId);
        }
        String brandId = (String) params.get("brandId");
        if(StringUtils.isNotEmpty(brandId) && !"0".equalsIgnoreCase(brandId)){
            queryWrapper.eq("brand_id",brandId);
        }
        String min = (String) params.get("min");
        if(StringUtils.isNotEmpty(min)){
            queryWrapper.ge("price",min);
        }
        String max = (String) params.get("max");
        if(StringUtils.isNotEmpty(max)){
            try{
                BigDecimal bigDecimal = new BigDecimal(max);
                if(bigDecimal.compareTo(new BigDecimal("0")) == 1){
                    queryWrapper.le("price",max);
                }
            }catch (Exception e){

            }
        }
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        List<SkuInfoEntity> list = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id",spuId));
        return list;
    }

    @Override
    public SkuItemVo item(Long skuId) {
        SkuItemVo skuItemVo = new SkuItemVo();
        // 获取sku基本信息
        SkuInfoEntity info = getById(skuId);
        skuItemVo.setInfo(info);

        // sku图片信息
        List<SkuImagesEntity> images = imagesService.getImagesBySkuId(skuId);
        skuItemVo.setImages(images);

        // 获取spu的销售属性组合

        // 获取spu的介绍 pms_spu_info_desc
        Long spuId = info.getSpuId();
        SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(spuId);
        skuItemVo.setDesc(spuInfoDescEntity);

        // 获取spu的规格参数信息
        Long catalogId = info.getCatalogId();
        List<SpuItemAttrGroupVo> attrGroupVos = attrGroupService.getAttrGroupWithAttrsBySpuId(spuId, catalogId);
        skuItemVo.setGroupAttrs(attrGroupVos);

        return skuItemVo;
    }

}