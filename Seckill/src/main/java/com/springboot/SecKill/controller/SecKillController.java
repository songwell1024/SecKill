package com.springboot.SecKill.controller;

import com.springboot.SecKill.domain.OrderInfo;
import com.springboot.SecKill.domain.SecKillOrder;
import com.springboot.SecKill.domain.SecKillUser;
import com.springboot.SecKill.result.CodeMsg;
import com.springboot.SecKill.result.Result;
import com.springboot.SecKill.service.GoodsService;
import com.springboot.SecKill.service.OrderService;
import com.springboot.SecKill.service.SecKillService;
import com.springboot.SecKill.service.SecKillUserService;
import com.springboot.SecKill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author WilsonSong
 * @date 2018/8/4/004
 */
@Controller
@RequestMapping("/miaosha")
public class SecKillController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    SecKillUserService secKillUserService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SecKillService secKillService;

	/**
	*4核 + 1g
	*QPS 1306
	*5000 *10 用户秒杀
	*/
    //商品列表页
    @RequestMapping(value = "/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> miaosha(Model model, SecKillUser user, @RequestParam("goodsId") long goodsId){
        model.addAttribute("user",user);
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

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
    }

}
