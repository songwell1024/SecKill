package com.springboot.SecKill.access;

import com.alibaba.fastjson.JSON;
import com.springboot.SecKill.domain.SecKillUser;
import com.springboot.SecKill.redis.AccessKey;
import com.springboot.SecKill.redis.RedisService;
import com.springboot.SecKill.result.CodeMsg;
import com.springboot.SecKill.result.Result;
import com.springboot.SecKill.service.SecKillUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * 拦截器
 * @author WilsonSong
 * @date 2018/8/9/009
 */
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter{
    @Autowired
    SecKillUserService secKillUserService;
    @Autowired
    RedisService redisService;

    //方法执行前执行
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            SecKillUser user = getUser(request,response);
            UserContext.setUser(user);       //把用户保存在本地线程变量中,并且该user与线程绑定一直执行到结束

            HandlerMethod handlerMethod = (HandlerMethod)handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);     //取方法上的注解
            if (accessLimit == null){
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();

            if (needLogin){
                if (user == null){
                    render(response,CodeMsg.SESSION_ERROR);
                    return false;
                }
                key +="_" + user.getId();
            }else {
                //da nothing
            }

            //访问次数限制 访问次数存入内存
            AccessKey accessKey = AccessKey.withExpires(seconds);
            Integer count = redisService.get(accessKey,key, Integer.class);
            if (count == null){
                redisService.set(accessKey,key, 1);
            }else if (count < maxCount){
                redisService.incr(accessKey,key);
            }else {
                render(response,CodeMsg.ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return true;
    }

    /**
     * 返回客户端的错误信息
     * @param response
     * @param cm
     * @throws Exception
     */
    public void render(HttpServletResponse response,CodeMsg cm) throws Exception{
        response.setContentType("application/json;charset=UTF-8");   //返回的数据的编码方式
        OutputStream outputStream = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(cm));
        outputStream.write(str.getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 通过cookie获取用户
     * @param request
     * @param response
     * @return
     */
    private SecKillUser getUser(HttpServletRequest request, HttpServletResponse response){
        String paramToken = request.getParameter(SecKillUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request,SecKillUserService.COOKIE_NAME_TOKEN);

        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return secKillUserService.getByToken(response,token);
    }

    /**
     * 获取cookie
     * @param request
     * @param cookieName
     * @return
     */
    private String getCookieValue(HttpServletRequest request,String cookieName){
        Cookie[] cookies = request.getCookies();

        if(cookies == null || cookies.length <= 0){
            return null;
        }
        for (Cookie cookie : cookies){
            if (cookie.getName().equals(cookieName)){
                return cookie.getValue();
            }
        }
        return null;
    }

}
