package com.springboot.SecKill.redis;

/**
 * @author WilsonSong
 * @date 2018/8/5/005
 */
public class GoodsKey extends BasePrefix{

    /**
     * key
     * @param expireSeconds 缓存的有效期
     * @param prefix 前缀
     */
    private GoodsKey(int expireSeconds, String prefix){
        super(expireSeconds,prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60,"gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(60,"gd");
}
