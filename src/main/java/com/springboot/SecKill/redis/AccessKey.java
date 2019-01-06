package com.springboot.SecKill.redis;

/**
 * @author WilsonSong
 * @date 2018/8/8
 */
public class AccessKey extends  BasePrefix{
    /**
     * @param expireSeconds 有效期
     * @param prefix 前缀
     */
    public AccessKey(int expireSeconds, String prefix){
        super(expireSeconds,prefix);
    }

    public static AccessKey withExpires(int expiresSeconds){
        return new AccessKey(expiresSeconds,"access");
    }

}
