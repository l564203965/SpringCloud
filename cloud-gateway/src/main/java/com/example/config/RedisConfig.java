package com.example.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author: ljy  Date: 2020/9/18.
 * redis限流处理
 */
@Configuration
public class RedisConfig {


    /**
     * 根据请求参数中的 user 字段来限流
     */
    @Bean(name = "userKeyResolver")
    KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("user"));
    }


    /**
     * 根据请求 IP 地址来限流
     */
    /*@Bean(name = "ipKeyResolver")
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }*/

    /**
     * 获取请求地址的uri作为限流key。
     * @return
     */
    /*@Bean
    KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }*/

}
