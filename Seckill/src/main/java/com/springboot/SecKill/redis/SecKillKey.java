package com.springboot.SecKill.redis;

/**
 * @author WilsonSong
 * @date 2018/8/8
 */
public class SecKillKey extends  BasePrefix{
    public SecKillKey(String prefix){
        super(prefix);
    }
    public static SecKillKey isGoodsOver = new SecKillKey("go");

}
