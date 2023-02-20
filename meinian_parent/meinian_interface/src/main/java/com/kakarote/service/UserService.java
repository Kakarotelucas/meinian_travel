package com.kakarote.service;

/**
 * @Date: 2023/2/20 11:38
 * @Auther: Kakarotelu
 * @Description:
 */

import com.kakarote.pojo.User;

/**
 * 用户服务接口
 */
public interface UserService {
    User findUserByUsername(String username);
}

