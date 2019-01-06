package com.springboot.SecKill.SMSVerification;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.springboot.SecKill.rabbitmq.MQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author WilsonSong
 * @date 2019/1/6/006
 */
@Component
public class SMSListener {

    @Autowired
    private SMSUtil smsUtil;

    @RabbitListener(queues = MQConfig.SMS_QUEUE)
    public void sendSms(Map<String,String> map){
        try {
            SendSmsResponse response = smsUtil.sendSms(map.get("mobile"),
                    map.get("template_code") ,
                    map.get("sign_name")  ,
                    map.get("param") );
            System.out.println("code:"+response.getCode());
            System.out.println("message:"+response.getMessage());


        } catch (ClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
