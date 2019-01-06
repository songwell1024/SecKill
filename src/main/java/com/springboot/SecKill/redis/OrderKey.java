package com.springboot.SecKill.redis;


/**
 * 订单模块的redis key
 * @author WilsonSong
 * @date 2018/8/1/001
 */
public class OrderKey extends  BasePrefix{

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public OrderKey( String prefix) {
        super( prefix);
    }

    public static OrderKey getSecKillOrderByUidGid = new OrderKey("moug");
}
