package com.example.filter;

import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.example.config.AuthProvider;
import com.example.props.AuthProperties;
import com.example.provider.ResponseProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author: ljy  Date: 2020/9/20.
 */
@Slf4j
@Component
@AllArgsConstructor
public class TokenFilter implements GlobalFilter, Ordered {

    private AuthProperties authProperties;
    private ObjectMapper objectMapper;
    private Map<String,String> params;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String tokenHeader = "authorization";
        String path = exchange.getRequest().getURI().getPath();
        if (isSkip(path)) {
            return chain.filter(exchange);
        }
        ServerHttpResponse resp = exchange.getResponse();

        String authToken = exchange.getRequest().getQueryParams().getFirst(tokenHeader);
        if (StringUtils.isBlank(authToken)) {
            authToken = exchange.getRequest().getHeaders().getFirst(tokenHeader);
        }
        if (StringUtils.isBlank(authToken)) {
            return unAuth(resp, "缺失令牌,鉴权失败");
        }
        return chain.filter(exchange);
    }

    private boolean isSkip(String path) {
        return AuthProvider.getDefaultSkipUrl().stream().map(url -> url.replace(AuthProvider.TARGET, AuthProvider.REPLACEMENT)).anyMatch(path::contains)
                || authProperties.getSkipUrl().stream().map(url -> url.replace(AuthProvider.TARGET, AuthProvider.REPLACEMENT)).anyMatch(path::contains);
    }

    private Mono<Void> unAuth(ServerHttpResponse resp, String msg) {
        resp.setStatusCode(HttpStatus.UNAUTHORIZED);
        resp.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        String result = "";
        try {
            result = objectMapper.writeValueAsString(ResponseProvider.unAuth(msg));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        DataBuffer buffer = resp.bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8));
        return resp.writeWith(Flux.just(buffer));
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
