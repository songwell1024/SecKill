package com.springboot.SecKill.rabbitmq;

import com.springboot.SecKill.domain.SecKillUser;

/**
 * @author WilsonSong
 * @date 2018/8/8/008
 */

public class SecKillMessage {
    private SecKillUser user;
    private long goodsId;

    public SecKillUser getUser() {
        return user;
    }

    public void setUser(SecKillUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
