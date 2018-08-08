package com.springboot.SecKill.rabbitmq;

import com.springboot.SecKill.domain.OrderInfo;
import com.springboot.SecKill.domain.SecKillOrder;
import com.springboot.SecKill.domain.SecKillUser;
import com.springboot.SecKill.redis.RedisService;
import com.springboot.SecKill.result.CodeMsg;
import com.springboot.SecKill.result.Result;
import com.springboot.SecKill.service.GoodsService;
import com.springboot.SecKill.service.OrderService;
import com.springboot.SecKill.service.SecKillService;
import com.springboot.SecKill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WilsonSong
 * @date 2018/8/8
 */
@Service
public class MQReceiver {

    private static final Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    SecKillService secKillService;

    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receive(String message){
        logger.info("receive message" + message);
        SecKillMessage secKillMessage = RedisService.String2Bean(message,SecKillMessage.class);
        SecKillUser user = secKillMessage.getUser();
        long goodsId = secKillMessage.getGoodsId();

        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0){
            return;
        }
        //判断是否已经秒杀到了
        SecKillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(),goodsId);

        if (order != null){
            return;
        }
        //减库存 下订单 写入秒杀订单
        //订单的详细信息
        OrderInfo orderInfo = secKillService.secKill(user, goods);
    }

//    /**
//     * Direct 交换机模式
//     */
//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void receive(String message){
//        logger.info("receive message" + message);
//    }
//
//    /**
//     * Topic 交换机模式
//     */
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//    public void receiveTopic1(String message){
//        logger.info("receive topic queue1 message: " + message);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//    public void receiveTopic2(String message){
//        logger.info("receive topic queue2 message: " + message);
//    }
//
//    /**
//     * Fanout模式 交换机Exchange
//     */
//    @RabbitListener(queues = MQConfig.FANOUT_QUEUE1)
//    public void receiveFanout1(String message){
//        logger.info("receive fanout queue1 message: " + message);
//    }
//
//    @RabbitListener(queues = MQConfig.FANOUT_QUEUE2)
//    public void receiveFanout2(String message){
//        logger.info("receive fanout queue2 message: " + message);
//    }
//
//    /**
//     * Header模式 交换机Exchange
//     */
//    @RabbitListener(queues = MQConfig.HEADERS_QUEUE)
//    public void receiveFanout2(byte[] message){
//        logger.info("receive headers queue message: " + new String(message));
//    }

}
