package com.example.core.redis;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author: ljy  Date: 2020/2/20.
 *
 */
@Service
public class RedisTool {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    private static final String LOCK_SUCCESS = "OK";
    private static final Long RELEASE_SUCCESS = 1L;
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    /**
     * 实用于redis集群主从备份
     * @param lockKey
     * @param requestId
     * @param expireTime
     * @throws InterruptedException
     */
    public void getDistributedLock(String lockKey, String requestId, int expireTime) throws InterruptedException {
        RLock lock = redissonClient.getLock(this.getClass().getSimpleName());

        // or wait for lock aquisition up to 100 seconds
        // and automatically unlock it after 10 seconds
        boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
        if (res) {
            try {
                //
            } finally {
                lock.unlock();
            }
        }
    }


    /**
     * 尝试获取分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     * 仅实用于单redis实例
     */
    public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            Jedis jedis = null;
            try {
                jedis = (Jedis) redisConnection.getNativeConnection();
                if (null != jedis) {
                    String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
                    if (LOCK_SUCCESS.equals(result)) {
                        return true;
                    }
                }
                return false;
            } catch (Exception e) {
            }finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
            return false;
        });
    }

    /**
     * 释放分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     * 仅实用于单redis实例
     */
    public boolean releaseDistributedLock(String lockKey, String requestId) {

        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            Jedis jedis = null;
            try {
                jedis = (Jedis) redisConnection.getNativeConnection();
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
                if (RELEASE_SUCCESS.equals(result)) {
                    return true;
                }
                return false;
            } catch (Exception e) {
            }finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
            return false;
        });



    }
}
