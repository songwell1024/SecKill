package com.springboot.SecKill.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5加密
 * @author WilsonSong
 * @date 2018/8/2/002
 */
public class MD5Util {
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";   //固定的salt

    public static String inputPass2FromPass(String inputPass) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String fromPass2DBPass(String fromPass, String salt) {

        String str = ""+salt.charAt(0)+salt.charAt(2) + fromPass +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    //两次MD5将用户密码存入数据库
    public static String inputPass2DBPass(String inputPass, String saltDB){
        String fromPass = inputPass2FromPass(inputPass);
        return fromPass2DBPass(fromPass,saltDB);
    }

//    public static void main(String[] args){
//        System.out.println(inputPass2DBPass("123456","1a2b3c4d"));
//    }
}
