package com.kakarote.dao;

import com.kakarote.pojo.User;
import org.springframework.stereotype.Repository;

/**
 * @Date: 2023/2/20 11:43
 * @Auther: Kakarotelu
 * @Description:
 */
@Repository
public interface UserDao {

    User findUserByUsername(String username);
}

