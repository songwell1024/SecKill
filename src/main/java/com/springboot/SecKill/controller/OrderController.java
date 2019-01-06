package com.springboot.SecKill.controller;

import com.springboot.SecKill.domain.OrderInfo;
import com.springboot.SecKill.domain.SecKillUser;
import com.springboot.SecKill.result.CodeMsg;
import com.springboot.SecKill.result.Result;
import com.springboot.SecKill.service.GoodsService;
import com.springboot.SecKill.service.OrderService;
import com.springboot.SecKill.vo.GoodsVo;
import com.springboot.SecKill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author WilsonSong
 * @date 2018/8/6
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, SecKillUser user, @RequestParam("orderId") long orderId){
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);

        }
        OrderInfo order = orderService.getOrderById(orderId);
        if (order == null){
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setGoods(goods);
        vo.setOrder(order);
        return  Result.success(vo);

    }


}


