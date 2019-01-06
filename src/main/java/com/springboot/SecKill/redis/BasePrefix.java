package com.springboot.SecKill.redis;

/**
 * redis的key的接口的抽象类，一般设为abstract
 * @author WilsonSong
 * @date 2018/8/1/001
 */
public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;   //过期时间
    private String prefix;  //前缀

    //设置为永不过期的key
    public BasePrefix( String prefix) {
        this(0, prefix);
    }

    public BasePrefix(int expireSeconds, String prefix){
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {
        return expireSeconds;  //默认0是永不过期的
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();     //获取类名 a.b(),得到的就是a
        return className + ":" + prefix;
    }
}
