package com.springboot.SecKill.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Redis的配置
 * @author WilsonSong
 * @date 2018/8/1/001
 */
//作为组件扫描进来
@Component
//读取配置文件
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig {
    private String host;  //主机
    private int port;  //端口
    private int timeout;  //超时时间

    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;  //连接池最大线程数

    @Value("${spring.redis.jedis.pool.max-wait}")
    private long  maxWait;  //等待时间

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;//最大空闲连接

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }





}
