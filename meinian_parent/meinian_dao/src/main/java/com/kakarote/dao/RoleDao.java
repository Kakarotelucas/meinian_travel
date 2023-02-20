package com.kakarote.dao;

/**
 * @Date: 2023/2/20 11:46
 * @Auther: Kakarotelu
 * @Description:
 */

import com.kakarote.pojo.Role;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 持久层Dao接口
 */
@Repository
public interface RoleDao {
    Set<Role> findRolesByUserId(Integer userId);
}

