package com.springboot.SecKill.vo;

import com.springboot.SecKill.domain.SecKillUser;

/**
 * 向页面传输数据的
 * @author WilsonSong
 * @date 2018/8/5
 */
public class GoodsDetailVo {

    private int SecKillStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods;
    private SecKillUser user;

    public SecKillUser getUser() {
        return user;
    }

    public void setUser(SecKillUser user) {
        this.user = user;
    }

    public int getSecKillStatus() {
        return SecKillStatus;
    }

    public void setSecKillStatus(int secKillStatus) {
        SecKillStatus = secKillStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }
}
