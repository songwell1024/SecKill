package com.springboot.SecKill.controller;

import com.springboot.SecKill.access.AccessLimit;
import com.springboot.SecKill.domain.OrderInfo;
import com.springboot.SecKill.domain.SecKillOrder;
import com.springboot.SecKill.domain.SecKillUser;
import com.springboot.SecKill.rabbitmq.MQSender;
import com.springboot.SecKill.rabbitmq.SecKillMessage;
import com.springboot.SecKill.redis.AccessKey;
import com.springboot.SecKill.redis.GoodsKey;
import com.springboot.SecKill.redis.RedisService;
import com.springboot.SecKill.redis.SecKillKey;
import com.springboot.SecKill.result.CodeMsg;
import com.springboot.SecKill.result.Result;
import com.springboot.SecKill.service.GoodsService;
import com.springboot.SecKill.service.OrderService;
import com.springboot.SecKill.service.SecKillService;
import com.springboot.SecKill.service.SecKillUserService;
import com.springboot.SecKill.util.MD5Util;
import com.springboot.SecKill.util.UUIDUtil;
import com.springboot.SecKill.vo.GoodsVo;
import com.sun.deploy.net.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author WilsonSong
 * @date 2018/8/4/004
 */
@Controller
@RequestMapping("/miaosha")
public class SecKillController implements InitializingBean{

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    SecKillUserService secKillUserService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    SecKillService secKillService;
    @Autowired
    RedisService redisService;
    @Autowired
    MQSender mqSender;

    private Map<Long, Boolean> localOverMap = new HashMap<>();

    /**
     * 系统初始化会调用该函数
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if (goodsVoList == null){
            return;
        }
        for (GoodsVo goodsVo:goodsVoList){
            //预先把商品库存加载到redis中
            redisService.set(GoodsKey.getSeckillGoodsStock,""+goodsVo.getId(),goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(),false);
        }
    }

	/**
	*4核 + 1g
	*QPS 1306
	*5000 *10 用户秒杀
	*/
    //商品列表页
    @RequestMapping(value = "/{path}/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model, SecKillUser user, @RequestParam("goodsId") long goodsId, @PathVariable("path") String path){
        model.addAttribute("user",user);
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //验证path
        boolean check = secKillService.checkPath(user,goodsId,path);
        if (!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if(over){
            return Result.error(CodeMsg.SECKILL_OVER);
        }


        //预先减库存
        long stock = redisService.decr(GoodsKey.getSeckillGoodsStock,""+goodsId);
        if (stock < 0){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //判断是否已经秒杀到了
        SecKillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(),goodsId);
        if (order != null){
            return Result.error( CodeMsg.SECKILL_REPEATE);
        }
        //压入RabbitMQ队列
        SecKillMessage secKillMessage = new SecKillMessage();
        secKillMessage.setUser(user);
        secKillMessage.setGoodsId(goodsId);
        mqSender.sendSecKillMessage(secKillMessage);
        return Result.success(0);    //排队中

        /*
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0){
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //判断是否已经秒杀到了
        SecKillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(),goodsId);

        if (order != null){
            return Result.error( CodeMsg.SECKILL_REPEATE);
        }

        //减库存 下订单 写入秒杀订单
        //订单的详细信息
        OrderInfo orderInfo = secKillService.secKill(user, goods);

        return Result.success(orderInfo);
         */
    }

    //秒杀的结果

    /**
     * orderId:秒杀成功
     * -1： 秒杀失败
     * 0：排队中
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, SecKillUser user, @RequestParam("goodsId") long goodsId){
        model.addAttribute("user",user);
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = secKillService.getSecKillResult(user.getId(),goodsId);
        return Result.success(result);
    }


    /**
     * 秒杀的验证码校验接口
     * @param user
     * @param goodsId
     * @param verifyCode
     * @return
     */
    @AccessLimit(seconds = 5,maxCount = 5, needLogin = true)
    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request, SecKillUser user,
                                         @RequestParam("goodsId") long goodsId,
                                         @RequestParam(value = "verifyCode", defaultValue = "0") int verifyCode){
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //验证码的校验
        boolean check = secKillService.checkVerifyCode(user,goodsId,verifyCode);
        if (!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path = secKillService.createSecKillPath(user,goodsId);
        return Result.success(path);
    }

    /**
     * 获取验证码
     * @param response
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/verifyCode",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCode(HttpServletResponse response, SecKillUser user, @RequestParam("goodsId") long goodsId){
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        BufferedImage  image = secKillService.createSecKillVerifyCode(user,goodsId);
        try{
            OutputStream out = response.getOutputStream();  //输出流
            ImageIO.write(image,"JPEG",out);  //图片写入输出流
            out.flush();
            out.close();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(CodeMsg.SECKILL_FAILED);
        }
    }
}


