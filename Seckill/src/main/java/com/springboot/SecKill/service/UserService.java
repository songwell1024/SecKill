package com.springboot.SecKill.service;

import com.springboot.SecKill.dao.UserDao;
import com.springboot.SecKill.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author WilsonSong
 * @date 2018/7/27/027
 */
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getById(int id){
       return userDao.getUserById(id);
    }

    //数据库事务测试,在同一个事务中的时候数据库中存在相同的数据时，所有的数据都不会被插入
    @Transactional
    public boolean tx(){
        User u1 = new User();
        u1.setId(2);
        u1.setName("Tom");
        userDao.insert(u1);

        User u2 = new User();
        u2.setId(1);
        u2.setName("Amy");
        userDao.insert(u2);
        return true;
    }

}
