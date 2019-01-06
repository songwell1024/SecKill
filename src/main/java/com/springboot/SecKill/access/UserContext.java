package com.springboot.SecKill.access;

import com.springboot.SecKill.domain.SecKillUser;

/**
 * @author WilsonSong
 * @date 2018/8/9/009
 */
public class UserContext {
    private static  ThreadLocal<SecKillUser> usrHolder = new ThreadLocal<>(); //ThreadLocal线程安全的

    public static void setUser(SecKillUser user){
        usrHolder.set(user);
    }

    public static SecKillUser getUser(){
        return usrHolder.get();
    }

}
