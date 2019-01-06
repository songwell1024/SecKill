package com.springboot.SecKill.controller;

import com.springboot.SecKill.domain.User;
import com.springboot.SecKill.result.CodeMsg;
import com.springboot.SecKill.result.Result;
import com.springboot.SecKill.service.SecKillUserService;
import com.springboot.SecKill.service.UserService;
import com.springboot.SecKill.util.ValidatorUtil;
import com.springboot.SecKill.vo.LoginVo;
import com.springboot.SecKill.vo.RegisterVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author WilsonSong
 * @date 2019/1/6/006
 * 注册
 */

@Controller
@RequestMapping("/register")
public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    SecKillUserService secKillUserService;
    @Autowired
    UserService userService;

    @RequestMapping("/sencCode")
    public Result<Boolean> sendCode(@Valid RegisterVo loginVo){
        try {
            logger.info(loginVo.toString());
            if(!ValidatorUtil.isMobile(loginVo.getMobile())){
                return Result.error(CodeMsg.MOBILE_ERROR);
            }
            userService.createSmsCode(loginVo.getMobile());
            return Result.success(true);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(CodeMsg.SMSCODE_ERROR);
        }
    }


    @RequestMapping("/add")
    public Result<Boolean> add(@RequestBody RegisterVo registerVo, String smsCode){
        try {
            boolean checkSmsCode = userService.checkSmsCode(registerVo.getMobile(), smsCode);
            if(!checkSmsCode){
                return Result.error(CodeMsg.SMSCODE_ERROR);
            }
            return Result.success(true);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(CodeMsg.SMSCODE_ERROR);
        }
    }
}
