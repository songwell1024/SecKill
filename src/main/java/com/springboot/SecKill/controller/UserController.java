package com.springboot.SecKill.controller;

import com.springboot.SecKill.domain.SecKillUser;
import com.springboot.SecKill.result.Result;
import com.springboot.SecKill.service.GoodsService;
import com.springboot.SecKill.service.SecKillUserService;
import com.springboot.SecKill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 商品页
 * @author WilsonSong
 * @date 2018/8/2/002
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    SecKillUserService secKillUserService;

    //商品列表页
    @RequestMapping("/info")
    @ResponseBody
    public Result<SecKillUser> info(Model model, SecKillUser user){
        model.addAttribute("user",user);
        return Result.success(user);
    }
}
