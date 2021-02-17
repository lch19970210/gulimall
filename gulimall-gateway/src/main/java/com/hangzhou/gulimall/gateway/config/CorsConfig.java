package com.hangzhou.gulimall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 添加CORS配置信息
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许的头信息
        corsConfiguration.addAllowedHeader("*");
        // 允许的请求方式
        corsConfiguration.addAllowedMethod("*");
        // 允许的域
        corsConfiguration.addAllowedOrigin("*");
        // 是否发送Cookie信息
        corsConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**",corsConfiguration);
        return new CorsWebFilter(source);
    }
}
