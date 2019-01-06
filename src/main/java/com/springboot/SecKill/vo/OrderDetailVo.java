package com.springboot.SecKill.vo;

import com.springboot.SecKill.domain.OrderInfo;

/**
 * @author WilsonSong
 * @date 2018/8/6/006
 */
public class OrderDetailVo {

    private GoodsVo goods;
    private OrderInfo order;

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }
}
