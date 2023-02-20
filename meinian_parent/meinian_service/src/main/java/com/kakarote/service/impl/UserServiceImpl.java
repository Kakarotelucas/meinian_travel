package com.kakarote.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.kakarote.dao.UserDao;
import com.kakarote.pojo.User;
import com.kakarote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Date: 2023/2/20 12:25
 * @Auther: Kakarotelu
 * @Description:
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    public User findUserByUsername(String username) {
        User user = userDao.findUserByUsername(username);
        return user;
    }
}

