package com.springboot.SecKill.controller;

import com.springboot.SecKill.domain.OrderInfo;
import com.springboot.SecKill.domain.SecKillOrder;
import com.springboot.SecKill.domain.SecKillUser;
import com.springboot.SecKill.result.CodeMsg;
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
import org.springframework.web.bind.annotation.RequestParam;


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

    //商品列表页
    @RequestMapping("/do_miaosha")
    public String list(Model model, SecKillUser user, @RequestParam("goodsId") long goodsId){
        model.addAttribute("user",user);
        if (user == null){
            return "login.html";
        }

        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0){
            model.addAttribute("errorMsg", CodeMsg.SECKILL_OVER.getMsg());
            return "miaosha_fail.html";
        }
        //判断是否已经秒杀到了
        SecKillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(),goodsId);

        if (order != null){
            model.addAttribute("errorMsg", CodeMsg.SECKILL_REPEATE.getMsg());
            return "miaosha_fail.html";
        }

        //减库存 下订单 写入秒杀订单
        //订单的详细信息
        OrderInfo orderInfo = secKillService.secKill(user, goods);
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goods);
        return "order_detail.html";
    }

}
