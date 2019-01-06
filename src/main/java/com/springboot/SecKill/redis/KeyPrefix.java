package com.springboot.SecKill.redis;

/**
 * redis的key的通用接口
 * @author WilsonSong
 * @date 2018/8/1/001
 */
public interface KeyPrefix {

    public int expireSeconds();

    public String getPrefix();

}
