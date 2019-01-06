//package com.springboot.SecKill.controller;
//
//import com.springboot.SecKill.rabbitmq.MQSender;
//import com.springboot.SecKill.result.Result;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
///**
// * @author WilsonSong
// * @date 2018/8/8/008
// */
//@Controller
//public class SampleController {
//
//
//    @Autowired
//    MQSender sender;
//
//
//    @RequestMapping("/rabbitmq")
//    @ResponseBody
//    public Result<String> rabbitmq(){
//        sender.send("yeah success");
//        return Result.success("哟哟哟");
//    }
//
//    @RequestMapping("/rabbitmq/topic")
//    @ResponseBody
//    public Result<String> topic(){
//        sender.sendTopic("yeah success");
//        return Result.success("哟哟哟");
//    }
//
//    @RequestMapping("/rabbitmq/fanout")
//    @ResponseBody
//    public Result<String> fanout(){
//        sender.sendFanout("yeah success");
//        return Result.success("哟哟哟");
//    }
//
//    @RequestMapping("/rabbitmq/header")
//    @ResponseBody
//    public Result<String> header(){
//        sender.sendHeaders("yeah success");
//        return Result.success("哟哟哟");
//    }
//}
