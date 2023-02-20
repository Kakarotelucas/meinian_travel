package com.kakarote.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kakarote.pojo.Permission;
import com.kakarote.pojo.Role;
import com.kakarote.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @Date: 2023/2/20 11:54
 * @Auther: Kakarotelu
 * @Description:实现认证和授权，注意要实现UserDetailsService接口
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference //注意：此处要通过dubbo远程调用用户服务
    private UserService userService;

    //根据用户名查询用户信息
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.远程调用用户服务，根据用户名查询用户信息，以及用户对应的角色，角色对应的权限
        com.kakarote.pojo.User user = userService.findUserByUsername(username);
        if (user == null) {
            //用户名不存在，抛出异常UsernameNotFoundException
            return null;
        }

        //2、构建权限集合
        Set<GrantedAuthority> authorities = new HashSet<>();

        //得到集合数据（由RoleDao帮忙方法来查询得到的）
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            ////得到集合数据（由permissionDao帮忙方法来查询得到的）获得每个角色对应的许可
            Set<Permission> permissions = role.getPermissions();

            for (Permission permission : permissions) {
                //授权
                authorities.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }

        /**
         * User()
         * 1：指定用户名
         * 2：指定密码（SpringSecurity会自动对密码进行校验）
         * 3：传递授予的角色和权限
         */
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        return userDetails;
    }
}
