package com.springboot.SecKill.service;

import com.springboot.SecKill.dao.SecKillUserDao;
import com.springboot.SecKill.domain.SecKillUser;
import com.springboot.SecKill.exception.GlobalException;
import com.springboot.SecKill.redis.RedisService;
import com.springboot.SecKill.redis.SecKillUserKey;
import com.springboot.SecKill.result.CodeMsg;
import com.springboot.SecKill.util.MD5Util;
import com.springboot.SecKill.util.UUIDUtil;
import com.springboot.SecKill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author WilsonSong
 * @date 2018/8/2
 */
@Service
public class SecKillUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    SecKillUserDao secKillUserDao;

    @Autowired
    RedisService redisService;

    public SecKillUser  getUserById(long id){
        //取缓存
        SecKillUser user = redisService.get(SecKillUserKey.getById,""+id, SecKillUser.class);
        if (user !=null){
            return user;
        }

        //缓存中没有从数据库中取出来放入缓存中
        user = secKillUserDao.getUserById(id);
        if (user != null){
            redisService.set(SecKillUserKey.getById,""+id, user);
        }
        return user;
    }

    //数据放在缓存中不过期，那有人修改了密码或者用户名怎么办
    public boolean updatePassword(String token,long id, String formPasswordNew){
        SecKillUser user = getUserById(id);
        if (user == null){
            throw  new GlobalException(CodeMsg.MOBILE_NOT_EXITS);
        }
        SecKillUser user2Update = new SecKillUser();
        user2Update.setId(id);
        user2Update.setPassword(MD5Util.fromPass2DBPass(formPasswordNew,user.getSalt()));
        secKillUserDao.update(user2Update);

        //修改缓存
        redisService.delete(SecKillUserKey.getById,""+id);
        //更新缓存中的token
        user.setPassword(user2Update.getPassword());
        redisService.set(SecKillUserKey.token,token, user);
        return true;
    }


    public SecKillUser getByToken(HttpServletResponse httpServletResponse,String token){
        if (StringUtils.isEmpty(token)){
            return null;
        }
        SecKillUser user= redisService.get(SecKillUserKey.token, token, SecKillUser.class);
        //重新登录的时候再重新生成一个cookie
        if (user != null){
            addCookie(httpServletResponse,token,user);
        }
        return user;
    }

    public Boolean login(HttpServletResponse httpServletResponse, LoginVo loginVo) {

        if (loginVo == null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String fromPass = loginVo.getPassword();

        //判断手机号是否存在
        SecKillUser user = getUserById(Long.parseLong(mobile));
        if (user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXITS);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass   = MD5Util.fromPass2DBPass(fromPass,saltDB);
        if (!calcPass.equals(dbPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(httpServletResponse,token,user);
        return true;

    }

    public void addCookie(HttpServletResponse httpServletResponse,String token,SecKillUser user){
        redisService.set(SecKillUserKey.token, token,user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(SecKillUserKey.token.expireSeconds());       //设置cookie的有效期与token的有效期相同
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }
}
