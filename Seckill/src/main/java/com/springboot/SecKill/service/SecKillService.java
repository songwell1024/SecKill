package com.springboot.SecKill.service;

import com.springboot.SecKill.dao.GoodsDao;
import com.springboot.SecKill.domain.Goods;
import com.springboot.SecKill.domain.OrderInfo;
import com.springboot.SecKill.domain.SecKillUser;
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

    //事务，原子性操作
    @Transactional
    public OrderInfo secKill(SecKillUser user, GoodsVo goods) {

        //减库存 下订单 写入秒杀订单 必须是同时完成的
        goodsService.reduceStock(goods);
        return orderService.createOrder(user,goods);


    }
}
