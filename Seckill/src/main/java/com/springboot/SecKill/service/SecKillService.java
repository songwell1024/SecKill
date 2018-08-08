package com.springboot.SecKill.service;

import com.springboot.SecKill.dao.GoodsDao;
import com.springboot.SecKill.domain.Goods;
import com.springboot.SecKill.domain.OrderInfo;
import com.springboot.SecKill.domain.SecKillOrder;
import com.springboot.SecKill.domain.SecKillUser;
import com.springboot.SecKill.redis.RedisService;
import com.springboot.SecKill.redis.SecKillKey;
import com.springboot.SecKill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 秒杀
 * @author WilsonSong
 * @date 2018/8/4/004
 */
@Service
public class SecKillService {

    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisService redisService;

    //事务，原子性操作
    @Transactional
    public OrderInfo secKill(SecKillUser user, GoodsVo goods) {

        //减库存 下订单 写入秒杀订单 必须是同时完成的
        boolean success = goodsService.reduceStock(goods);
        if (success) {
            return orderService.createOrder(user, goods);
        }else{
            setGoodsOver(goods.getId());
            return null;
        }
    }

    //获取结果
    /**
     * orderId :成功
     * -1:失败
     * 0： 排队中
     * @param userId
     * @param goodsId
     * @return
     */
    public  long getSecKillResult(Long userId, long goodsId) {
        SecKillOrder order = orderService.getOrderByUserIdGoodsId(userId,goodsId);
        if (order != null){
            return order.getOrderId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver){
                return -1;
            }else {
                return 0;
            }
        }

    }

    public void setGoodsOver(Long goodsId) {
        redisService.set(SecKillKey.isGoodsOver,""+goodsId,true);
    }

    public boolean getGoodsOver(Long goodsId) {
        return redisService.exists(SecKillKey.isGoodsOver,""+goodsId);
    }
}
