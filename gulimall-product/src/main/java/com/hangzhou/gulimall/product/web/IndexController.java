package com.hangzhou.gulimall.product.web;

import com.hangzhou.gulimall.product.entity.CategoryEntity;
import com.hangzhou.gulimall.product.service.CategoryService;
import com.hangzhou.gulimall.product.vo.Catelog2Vo;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author linchenghui
 * @Date 2021/3/15
 */
@Controller
public class IndexController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    RedissonClient redisson;
    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){
        // 查出所有的一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categorys();

        model.addAttribute("categorys",categoryEntities);
        // 试图解析器会拼接前缀和后缀
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Object getCatalogJson(){
        Map<String,List<Catelog2Vo>> map = categoryService.getCatalogJson();
        return map;
    }

    @ResponseBody
    @GetMapping("/hello")
    public String hello(){
        // 获取锁
        RLock lock = redisson.getLock("myLock");
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getId()+" 加锁成功,执行业务...");
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            System.out.println(Thread.currentThread().getId()+" 释放锁");
            lock.unlock();
        }
        return "hello";
    }

    @ResponseBody
    @GetMapping("/write")
    public String write(){
        // 获取锁
        RReadWriteLock myLock = redisson.getReadWriteLock("rdLock");
        String s = "";
        RLock lock = myLock.writeLock();
        try {
            // 改数据加写锁,读数据时加读锁
            lock.lock();
            System.out.println("线程 " + Thread.currentThread().getId()+" 写锁加锁成功");
            s = UUID.randomUUID().toString();
            Thread.sleep(30000);
            redisTemplate.opsForValue().set("writeLock",s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
            System.out.println("线程 " + Thread.currentThread().getId()+" 写锁解锁成功");
        }
        return s;
    }

    @ResponseBody
    @GetMapping("/read")
    public String read(){
        // 获取锁
        RReadWriteLock myLock = redisson.getReadWriteLock("rdLock");
        String s = "";
        RLock lock = myLock.readLock();
        lock.lock();
        System.out.println("线程 " + Thread.currentThread().getId()+" 读锁加锁成功");
        try {
            // 改数据加写锁,读数据时加读锁
            s = redisTemplate.opsForValue().get("writeLock");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
            System.out.println("线程 " + Thread.currentThread().getId()+" 读锁解锁成功");
        }
        return s;
    }
}
