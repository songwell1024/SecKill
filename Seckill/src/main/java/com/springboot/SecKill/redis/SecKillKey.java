package com.springboot.SecKill.redis;

/**
 * @author WilsonSong
 * @date 2018/8/8
 */
public class SecKillKey extends  BasePrefix{
    /**
     * @param expireSeconds 有效期
     * @param prefix 前缀
     */
    public SecKillKey(int expireSeconds,String prefix){
        super(expireSeconds,prefix);
    }
    public static SecKillKey isGoodsOver = new SecKillKey(0,"go");
    public static SecKillKey getPath = new SecKillKey(60,"gp");
    public static SecKillKey getSecKillVerifyCode = new SecKillKey(300,"gc");


}
