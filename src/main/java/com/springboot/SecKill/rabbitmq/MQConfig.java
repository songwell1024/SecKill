package com.springboot.SecKill.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Backoff;

import java.util.HashMap;
import java.util.Map;

/**
 * rabbitMQ配置初始化
 * @author WilsonSong
 * @date 2018/8/8
 */
@Configuration
public class MQConfig {

    public static final String SECKILL_QUEUE = "SecKill.QUEUE";
    public static final String SMS_QUEUE = "Sms.QUEUE";
    public static final String QUEUE = "queue";
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String FANOUT_QUEUE1 = "fanout.queue1";
    public static final String FANOUT_QUEUE2 = "fanout.queue2";
    public static final String HEADERS_QUEUE = "headers.queue";
    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String FANOUT_EXCHANGE = "fanoutExchange";
    public static final String HEADERS_EXCHANGE = "headersExchange";


    /**
     * Direct 交换机模式
     */
    //队列
    @Bean
    public Queue secKill_QUEUE() {
        return new Queue(SMS_QUEUE,true);
    }

    @Bean
    public Queue Sms_QUEUE() {
        return new Queue(SECKILL_QUEUE,true);
    }

    /**
     * Direct 交换机模式
     */
    //队列
    @Bean
    public Queue queue() {
        return new Queue(QUEUE,true);
    }

    /**
     * Topic 交换机模式
     */
    @Bean
    public Queue topicQueue1(){
        return new Queue(TOPIC_QUEUE1,true);
    }
    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUE2,true);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }
    /**
     * 绑定Exchange和queue
     */
    @Bean
    public Binding topicBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
    }
    @Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#"); //# 通配符，代表多个单词
    }

    /**
     * Fanout模式 交换机Exchange
     */
    @Bean
    public Queue fanoutQueue1(){
        return new Queue(FANOUT_QUEUE1,true);
    }
    @Bean
    public Queue fanoutQueue2(){
        return new Queue(FANOUT_QUEUE2,true);
    }
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }
    @Bean
    public Binding fanoutBinding1(){
        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBinding2(){
        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
    }

    /**
     * Header模式 交换机Exchange
     */
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERS_EXCHANGE);
    }
    @Bean
    public Queue headersQueue(){
        return new Queue(HEADERS_QUEUE,true);
    }
    @Bean
    public Binding headersBinding(){
        Map<String, Object> map = new HashMap<>();
        map.put("header1","value1");
        map.put("header2","value2");
        return BindingBuilder.bind(headersQueue()).to(headersExchange()).whereAll(map).match();
    }



}
