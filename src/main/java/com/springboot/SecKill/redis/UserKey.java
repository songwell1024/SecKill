package com.springboot.SecKill.redis;


/**
 * 用户模块的redis key
 * @author WilsonSong
 * @date 2018/8/1/001
 */
public class UserKey extends BasePrefix {


    private UserKey( String prefix) {
        super( prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}