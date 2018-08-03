package com.springboot.SecKill.util;


import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 判断数据格式
 * @author WilsonSong
 * @date 2018/8/2
 */
public class ValidatorUtil {

    //手机号的格式
    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src){
        if(StringUtils.isEmpty(src)){
            return false;
        }
        Matcher m = mobile_pattern.matcher(src);
        return m.matches();
    }

}
