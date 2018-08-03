package com.springboot.SecKill.dao;

import com.springboot.SecKill.domain.SecKillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author WilsonSong
 * @date 2018/8/2/002
 */
@Mapper
public interface SecKillUserDao {

    @Select("select * from miaosha_user where id = #{id}")
    public SecKillUser getUserById(@Param("id") long id);

}
