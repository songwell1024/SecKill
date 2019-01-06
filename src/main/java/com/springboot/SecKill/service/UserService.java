package com.springboot.SecKill.service;

import com.springboot.SecKill.dao.UserDao;
import com.springboot.SecKill.domain.User;
import com.springboot.SecKill.rabbitmq.MQConfig;
import com.springboot.SecKill.redis.RedisService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author WilsonSong
 * @date 2018/7/27/027
 */
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    RedisService redisService;
    @Autowired
    AmqpTemplate amqpTemplate;

    public User getById(int id){
       return userDao.getUserById(id);
    }


    //短信验证码服务
    public void createSmsCode(String phone){
        String  smscode = (long)(Math.random()*1000000) + "";
        redisService.setValue(phone,smscode);

        //将短信发送给RabbitMQ
        amqpTemplate.convertAndSend(MQConfig.SMS_QUEUE, smscode);

    }

    //短信验证码验证信息
    public boolean checkSmsCode(String phone, String code){
        String systemCode  = redisService.getValue(phone);
        if (systemCode == null){
            return false;
        }

        if (systemCode.equals(code)){
            return true;
        }else {
            return false;
        }

    }

    //数据库事务测试,在同一个事务中的时候数据库中存在相同的数据时，所有的数据都不会被插入
    @Transactional
    public boolean tx(){
        User u1 = new User();
        u1.setId(2);
        u1.setName("Tom");
        userDao.insert(u1);

        User u2 = new User();
        u2.setId(1);
        u2.setName("Amy");
        userDao.insert(u2);
        return true;
    }

}
