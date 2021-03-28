package com.hangzhou.gulimall.product.service.impl;

import com.hangzhou.gulimall.product.service.CategoryBrandRelationService;
import com.hangzhou.gulimall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hangzhou.common.utils.PageUtils;
import com.hangzhou.common.utils.Query;

import com.hangzhou.gulimall.product.dao.CategoryDao;
import com.hangzhou.gulimall.product.entity.CategoryEntity;
import com.hangzhou.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        List<CategoryEntity> level1Menus = entities.stream().filter(entity ->
                entity.getParentCid() == 0
        ).map(menu -> {
            menu.setChildren(getChildrens(menu,entities));
            return menu;
        }).sorted((menu1,menu2) -> {
            return (menu1.getSort() == null?0:menu1.getSort()) - (menu2.getSort() == null?0:menu2.getSort());
        }).collect(Collectors.toList());
        // 组装成树形结构
        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //todo 检查当前删除的菜单是否被别的地方引用

        // 逻辑删除

        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> path = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, path);
        // 逆序
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有关联的数据
     * @param category 分类数据
     */
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
    }

    @Override
    public Map<String, Object> getCatalogJson() {

        // 将原先多次查询数据库该成一次查询数据库
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);

        // 查出所有一级分类
//        List<CategoryEntity> level1Categorys = this.getLevel1Categorys();
        List<CategoryEntity> level1Categorys = getCategoryByCatId(categoryEntities,0L);
        // 封装数据
        Map<String, Object> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // 查询每个一级分类的二级分类
            List<CategoryEntity> level2Categories = getCategoryByCatId(categoryEntities,v.getCatId());
            // 封装数据
            List<Catelog2Vo> catelog2Vos = null;
            if(level2Categories != null){
                catelog2Vos = level2Categories.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    // 查询每个二级分类的三级分类
                    List<CategoryEntity> level3Catelog = getCategoryByCatId(categoryEntities,l2.getCatId());
                    if (level3Catelog != null) {
                        List<Catelog2Vo.Catelog3Vo> collect = level3Catelog.stream().map(l3 -> {
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(collect);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
        return parent_cid;
    }

    private List<CategoryEntity> getCategoryByCatId(List<CategoryEntity> categoryEntities,Long parentCatId){
        List<CategoryEntity> collect = categoryEntities.stream().filter(item -> item.getParentCid() == parentCatId).collect(Collectors.toList());
        return collect;
    }

    /**
     * 利用递归根据自身菜单id查找出所有父类菜单id
     * @param catelogId 自身菜单id
     * @param paths 存放联级菜单id
     * @return 当前菜单的联级菜单id数组
     */
    private List<Long> findParentPath(Long catelogId,List<Long> paths){
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if(byId.getParentCid() != 0){
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;
    }

    /**
     * 递归查找当前菜单的子菜单
     * @param root 当前菜单对象
     * @param allCategory 所有菜单对象
     * @return 树形结构的菜单对象
     */
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> allCategory){
        List<CategoryEntity> childrens = allCategory.stream().filter(entity -> {
            return entity.getParentCid() == root.getCatId();
        }).map(menu -> {
            menu.setChildren(getChildrens(menu, allCategory));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null?0:menu1.getSort()) - (menu2.getSort() == null?0:menu2.getSort());
        }).collect(Collectors.toList());
        return childrens;
    }
}