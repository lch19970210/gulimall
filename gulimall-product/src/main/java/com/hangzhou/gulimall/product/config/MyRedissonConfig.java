package com.hangzhou.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Author linchenghui
 * @Date 2021/4/1
 */
@Configuration
public class MyRedissonConfig {
    @Bean
    public RedissonClient redisson()  {
        // 创建配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://115.29.212.18:6379");
        // 根据config创建出实例对象
        return Redisson.create(config);
    }
}
