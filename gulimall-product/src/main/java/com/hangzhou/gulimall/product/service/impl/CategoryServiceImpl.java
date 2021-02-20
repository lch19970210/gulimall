package com.hangzhou.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hangzhou.common.utils.PageUtils;
import com.hangzhou.common.utils.Query;

import com.hangzhou.gulimall.product.dao.CategoryDao;
import com.hangzhou.gulimall.product.entity.CategoryEntity;
import com.hangzhou.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

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