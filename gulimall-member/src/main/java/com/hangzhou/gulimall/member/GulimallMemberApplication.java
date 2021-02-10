package com.hangzhou.gulimall.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 开启远程调用功能 @EnableFeignClients，要指定远程调用功能放的基础包
 */
@EnableFeignClients(basePackages="com.hangzhou.gulimall.member.feign")
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.hangzhou.gulimall.member.dao")
public class GulimallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallMemberApplication.class, args);
    }

}
