package com.example.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author: ljy  Date: 2020/9/18.
 */
public class UriKeyResolver  implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getURI().getPath());
    }



    @Bean
    public UriKeyResolver uriKeyResolver() {
        return new UriKeyResolver();
    }
}