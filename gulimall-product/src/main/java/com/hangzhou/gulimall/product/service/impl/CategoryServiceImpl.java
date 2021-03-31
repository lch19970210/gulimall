package com.hangzhou.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hangzhou.gulimall.product.service.CategoryBrandRelationService;
import com.hangzhou.gulimall.product.vo.Catelog2Vo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
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

    @Autowired
    StringRedisTemplate redisTemplate;

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
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        // 序列化与反序列化
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if(StringUtils.isEmpty(catalogJSON)){
            // 缓存没有命中,查询数据库后将对象转为json放入缓存当中
            System.out.println("缓存未命中...查询数据库...");
            Map<String, List<Catelog2Vo>> catalogJsonFromDb = getCatalogJsonFromDbWithRedisLock();
            redisTemplate.opsForValue().set("catalogJSON", JSON.toJSONString(catalogJsonFromDb),1, TimeUnit.DAYS);
            return catalogJsonFromDb;
        }
        System.out.println("缓存命中...");
        // 缓存命中再转为指定的对象
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {});
        return result;
    }

    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithRedisLock() {

        // 占用分布式锁
        String uuid = UUID.randomUUID().toString();
//        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", "ccc");
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid,300,TimeUnit.SECONDS);
        if(lock){
            System.out.println("获取分布式锁成功...");
            // 设置过期时间必须和占锁同时执行,否则在占锁后还未加入过期时间之间服务崩溃的话还是会造成死锁问题
            //redisTemplate.expire("lock",30,TimeUnit.SECONDS);
            // 加锁成功后执行业务(如果业务中抛异常会导致死锁问题)
            Map<String, List<Catelog2Vo>> dataFromDb ;
            try{
                dataFromDb = getDataFromDb();
            }finally {
                String lockValue = redisTemplate.opsForValue().get("lock");
                String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                        "    return redis.call(\"del\",KEYS[1])\n" +
                        "else\n" +
                        "    return 0\n" +
                        "end";
                redisTemplate.execute(
                        new DefaultRedisScript<Long>(script, Long.class),
                        Arrays.asList("lock"),
                        lockValue);
            }
            // 因为获取值比较,比较成功后删除不是一个原子操作,所以在传递删除锁操作前值过期,还是会引起相关问题
//            String lockValue = redisTemplate.opsForValue().get("lock");
//            if(uuid.equals(lockValue)){
//                redisTemplate.delete("lock");
//            }
            return dataFromDb;
        }else {
            System.out.println("获取分布式锁失败...等待重试");
            // 加锁失败后重试
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 自旋重试
            return getCatalogJsonFromDbWithRedisLock();
        }
    }

    private Map<String, List<Catelog2Vo>> getDataFromDb() {
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if (!StringUtils.isEmpty(catalogJSON)) {
            Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
            return result;
        }
        System.out.println("正在查询数据库...");

        // 将原先多次查询数据库该成一次查询数据库
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);

        // 查出所有一级分类
//        List<CategoryEntity> level1Categorys = this.getLevel1Categorys();
        List<CategoryEntity> level1Categorys = getCategoryByCatId(categoryEntities, 0L);
        // 封装数据
        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // 查询每个一级分类的二级分类
            List<CategoryEntity> level2Categories = getCategoryByCatId(categoryEntities, v.getCatId());
            // 封装数据
            List<Catelog2Vo> catelog2Vos = null;
            if (level2Categories != null) {
                catelog2Vos = level2Categories.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    // 查询每个二级分类的三级分类
                    List<CategoryEntity> level3Catelog = getCategoryByCatId(categoryEntities, l2.getCatId());
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

        String s = JSON.toJSONString(parent_cid);
        redisTemplate.opsForValue().set("catalogJSON", s, 1, TimeUnit.DAYS);
        return parent_cid;
    }

    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithLocalLock() {

        // 获取当前ServiceImpl实例的锁,但是只能锁住当前线程
        synchronized (this){
            return getDataFromDb();
        }
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