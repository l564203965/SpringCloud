package com.example.core.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: ljy  Date: 2020/2/20.
 * 实用于redis集群主从备份
 */
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // 单实例
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        // 集群
        /*config.useClusterServers()
                // cluster state scan interval in milliseconds
                .setScanInterval(2000)
                .addNodeAddress("redis://127.0.0.1:6379", "redis://127.0.0.1:6379")
                .addNodeAddress("redis://127.0.0.1:6379");*/
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
