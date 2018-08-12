package com.springboot.SecKill.service;

import com.springboot.SecKill.dao.GoodsDao;
import com.springboot.SecKill.domain.Goods;
import com.springboot.SecKill.domain.OrderInfo;
import com.springboot.SecKill.domain.SecKillOrder;
import com.springboot.SecKill.domain.SecKillUser;
import com.springboot.SecKill.redis.RedisService;
import com.springboot.SecKill.redis.SecKillKey;
import com.springboot.SecKill.util.MD5Util;
import com.springboot.SecKill.util.UUIDUtil;
import com.springboot.SecKill.vo.GoodsVo;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

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
        //减库存成功了才进行下订单
        if (success) {
            return orderService.createOrder(user, goods);
        }else{ //说明商品秒杀完了。做一个标记
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

    /**
     * 验证秒杀接口参数
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    public  boolean checkPath(SecKillUser user, long goodsId, String path) {
        if (user == null || path == null){
            return false;
        }
        String pathOld = redisService.get(SecKillKey.getPath,""+user.getId()+"_"+goodsId,String.class);
        return path.equals(pathOld);
    }

    public  String createSecKillPath(SecKillUser user, Long goodsId) {
        if (user == null || goodsId <= 0){
            return null;
        }
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(SecKillKey.getPath,user.getId()+"_"+goodsId,str);
        return str;
    }

    public BufferedImage createSecKillVerifyCode(SecKillUser user, long goodsId) {
        if (user == null || goodsId <= 0){
            return null;
        }
        int width = 80;
        int height = 32;
        //生成图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // 背景
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // 背景上生成矩形框
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // 随机数
        Random rdm = new Random();
        // 生成干扰点
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // 生成验证码
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(SecKillKey.getSecKillVerifyCode, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;

    }

    private static char[] ops = new char[] {'+', '-', '*'};
    /**
     * 生成验证码公式
     * + - *
     * */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    /**
     * Java ScriptEngine 解析js计算验证码
     * @param exp 验证码
     * @return
     */
    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 验证码的验证
     * @param user 用户
     * @param goodsId 商品id
     * @param verifyCode 验证码
     * @return
     */
    public boolean checkVerifyCode(SecKillUser user, long goodsId, int verifyCode) {
        if (user == null || goodsId <= 0){
            return false;
        }
        Integer codeOld = redisService.get(SecKillKey.getSecKillVerifyCode, user.getId()+","+goodsId, Integer.class);
        if (codeOld == null || codeOld - verifyCode != 0){
            return false;
        }
        //把当前的验证码清除
        redisService.delete(SecKillKey.getSecKillVerifyCode, user.getId()+","+goodsId);
        return true;

    }
}
