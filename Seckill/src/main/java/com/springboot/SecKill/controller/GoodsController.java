package com.springboot.SecKill.controller;

import com.springboot.SecKill.domain.SecKillUser;
import com.springboot.SecKill.redis.GoodsKey;
import com.springboot.SecKill.redis.RedisService;
import com.springboot.SecKill.service.GoodsService;
import com.springboot.SecKill.service.SecKillUserService;
import com.springboot.SecKill.util.SpringWebContextUtil;
import com.springboot.SecKill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


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

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

	/**
	 * 4核 1g
	 * QPS 1267
	 * 5000 *10 5000个线程起10次
	 */
	
    //商品列表页 不返回页面，直接返回HTML的代码
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, SecKillUser user){
        model.addAttribute("user",user);
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
        if (!StringUtils.isEmpty(html)){
            return html;
        }

        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);


        //缓存中没有数据的时候手动渲染
        SpringWebContextUtil ctx  = new SpringWebContextUtil(request, response, request.getServletContext(),request.getLocale(),model.asMap(),applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goodslist.html",ctx);

        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }

    //商品详情页
    @RequestMapping(value = "/to_detail/{goodsId}", produces = "text/html")
    @ResponseBody
    public String detail(HttpServletRequest request, HttpServletResponse response,Model model, SecKillUser user, @PathVariable("goodsId") long goodsId){
        model.addAttribute("user",user);

        //取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail,""+goodsId,String.class);
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        //手动渲染

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


        //缓存中没有数据的时候手动渲染
        SpringWebContextUtil ctx  = new SpringWebContextUtil(request, response, request.getServletContext(),
                                                             request.getLocale(),model.asMap(),applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail.html",ctx);

        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail,""+goodsId ,html);
        }

        return html;
}
}
