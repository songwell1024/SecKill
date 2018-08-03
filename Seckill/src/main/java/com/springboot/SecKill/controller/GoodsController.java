package com.springboot.SecKill.controller;

import com.springboot.SecKill.domain.SecKillUser;
import com.springboot.SecKill.result.Result;
import com.springboot.SecKill.service.GoodsService;
import com.springboot.SecKill.service.SecKillUserService;
import com.springboot.SecKill.vo.GoodsVo;
import com.springboot.SecKill.vo.LoginVo;
import jdk.nashorn.internal.parser.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.List;

import static com.springboot.SecKill.service.SecKillUserService.COOKIE_NAME_TOKEN;

/**
 * 商品页
 * @author WilsonSong
 * @date 2018/8/2/002
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    SecKillUserService secKillUserService;

    @Autowired
    GoodsService goodsService;

    //商品列表页
    @RequestMapping("/to_list")
    public String list(Model model,SecKillUser user){
        model.addAttribute("user",user);
        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goodslist.html";
    }

    //商品详情页
    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, SecKillUser user, @PathVariable("goodsId") long goodsId){
        model.addAttribute("user",user);

       GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
       model.addAttribute("goods",goods);

       //秒杀的详细信息
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis(); //当前的时间

        int SecKillStatus = 0;
        int remainSeconds = 0;
        if (now < startAt){  //秒杀未开始
            SecKillStatus = 0;
            remainSeconds = (int)((startAt - now)/1000);
        }else if (now > endAt){  //秒杀结束
            SecKillStatus = 2;
            remainSeconds = -1;
        }else {
            SecKillStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("miaoshaStatus",SecKillStatus);
        model.addAttribute("remainSeconds",remainSeconds);
        return "goods_detail.html";
}
}
