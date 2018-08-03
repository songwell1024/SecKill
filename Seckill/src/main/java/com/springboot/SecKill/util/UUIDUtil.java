package com.springboot.SecKill.util;

import java.util.UUID;

/**
 * @author WilsonSong
 * @date 2018/8/3/003
 */
public class UUIDUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
