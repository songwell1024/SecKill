package com.springboot.SecKill.rabbitmq;

import com.springboot.SecKill.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WilsonSong
 * @date 2018/8/8
 */
@Service
public class MQSender {

    private static final Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendSecKillMessage(SecKillMessage secKillMessage) {
        String msg = RedisService.Bean2String(secKillMessage);
        logger.info("send SecKill message: " + msg);
        amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE, msg);

    }

//
//    /**
//     * Direct 交换机模式
//     */
//    //消息发送到队列
//    public void send(Object message){
//
//        String msg = RedisService.Bean2String(message);
//        logger.info("send topic message: " + msg);
//        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
//    }
//
//    /**
//     * Topic 交换机模式
//     */
//    public void sendTopic(Object message){
//        String msg = RedisService.Bean2String(message);
//        logger.info("send topic message: " + msg);
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key1",msg+"1");
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key2",msg+"1");
//    }
//
//    /**
//     * Fanout模式 交换机Exchange
//     */
//    public void sendFanout(Object message){
//        String msg = RedisService.Bean2String(message);
//        logger.info("send fanout message: " + msg);
//        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg+"1");
//    }
//
//    /**
//     * Header模式 交换机Exchange
//     *"header1","value1"要与队列初始化的时候一样
//     */
//    public void sendHeaders(Object message){
//        String msg = RedisService.Bean2String(message);
//        logger.info("send headers message: " + msg);
//        MessageProperties properties = new MessageProperties();
//        properties.setHeader("header1","value1");
//        properties.setHeader("header2","value2");
//        Message obj = new Message(msg.getBytes(),properties);
//        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE,"",obj);
//    }



}
