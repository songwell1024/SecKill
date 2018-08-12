package com.springboot.SecKill.config;

import com.springboot.SecKill.access.AccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * web页面的参数配置
 * @author WilsonSong
 * @date 2018/8/3/003
 */
@Configuration
public class webConfig implements WebMvcConfigurer {

    @Autowired
    UserArgumentResolver argumentResolver;

    @Autowired
    AccessInterceptor accessInterceptor;

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(argumentResolver);
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessInterceptor);
    }

}
