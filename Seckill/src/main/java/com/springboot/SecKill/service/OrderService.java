package com.springboot.SecKill.service;

import com.springboot.SecKill.dao.OrderDao;
import com.springboot.SecKill.domain.OrderInfo;
import com.springboot.SecKill.domain.SecKillOrder;
import com.springboot.SecKill.domain.SecKillUser;
import com.springboot.SecKill.redis.OrderKey;
import com.springboot.SecKill.redis.RedisService;
import com.springboot.SecKill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 订单
 * @author WilsonSong
 * @date 2018/8/4/004
 */
@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    RedisService redisService;

    public  SecKillOrder getOrderByUserIdGoodsId(long userId, long goodsId) {
        return redisService.get(OrderKey.getSecKillOrderByUidGid,""+userId+"_"+goodsId,SecKillOrder.class);
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }

    @Transactional
    public OrderInfo createOrder(SecKillUser user, GoodsVo goods) {

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        long orderId = orderDao.insert(orderInfo);

        SecKillOrder secKillOrder = new SecKillOrder();
        secKillOrder.setGoodsId(goods.getId());
        secKillOrder.setOrderId(orderId);
        secKillOrder.setUserId(user.getId());
        orderDao.insertSecKillOrder(secKillOrder);

        redisService.set(OrderKey.getSecKillOrderByUidGid,""+user.getId()+"_"+goods.getId(),secKillOrder);
        return orderInfo;
    }


}
