package com.example.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * @author: ljy  Date: 2020/1/20.
 */
public class EnvConfig implements EnvironmentAware {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Environment env;

    public String getStringValue(String key) {
        return env.getProperty(key);
    }

    public Long getLongValue(String key) {
        String value = getStringValue(key);
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            logger.error("字符串转换Long失败：{} = {}", key, value);
        }
        return 0L;
    }

    public int getIntValue(String key) {
        return getLongValue(key).intValue();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

}
