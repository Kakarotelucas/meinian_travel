package com.kakarote.dao;

/**
 * @Date: 2023/2/20 11:46
 * @Auther: Kakarotelu
 * @Description:
 */

import com.kakarote.pojo.Permission;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 持久层Dao接口
 */
@Repository
public interface PermissionDao {
    Set<Permission> findPermissionsByRoleId(Integer roleId);
}

