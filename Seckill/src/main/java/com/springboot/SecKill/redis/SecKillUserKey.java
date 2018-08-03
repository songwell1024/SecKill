package com.springboot.SecKill.redis;

/**
 * @author WilsonSong
 * @date 2018/8/3/003
 */
public class SecKillUserKey extends BasePrefix {

    private static final int TOKEN_EXPIRE = 3600*24;  //token的过期时间
    private SecKillUserKey(int expireSeconds,String prefix){
        super(expireSeconds,prefix);
    }

    public static SecKillUserKey token = new SecKillUserKey(TOKEN_EXPIRE,"tk");
}
