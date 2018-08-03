package com.springboot.SecKill.controller;

import com.springboot.SecKill.domain.SecKillUser;
import com.springboot.SecKill.result.CodeMsg;
import com.springboot.SecKill.result.Result;
import com.springboot.SecKill.service.SecKillUserService;
import com.springboot.SecKill.util.ValidatorUtil;
import com.springboot.SecKill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 登陆
 * @author WilsonSong
 * @date 2018/8/2/002
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    SecKillUserService secKillUserService;

    @RequestMapping("/to_login")
    public String tologin(){
        return "login.html";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    //@Valid是JSR303校验
    public Result<Boolean> dologin(HttpServletResponse httpServletResponse,@Valid LoginVo loginVo){
        logger.info(loginVo.toString());
//        //参数校验
//        String inputPassword = loginVo.getPassword();
//        String mobile = loginVo.getMobile();
//        if(StringUtils.isEmpty(inputPassword)){
//            return Result.error(CodeMsg.PASSWORD_EMPTY);
//        }
//
//        if(StringUtils.isEmpty(mobile)){
//            return Result.error(CodeMsg.MOBILE_EMPTY);
//        }
//
//        //检查手机号的格式
//        if(!ValidatorUtil.isMobile(mobile)){
//            return Result.error(CodeMsg.MOBILE_ERROR);
//
//        }
        //登陆
        secKillUserService.login(httpServletResponse,loginVo);
        return Result.success(true);
    }


}
