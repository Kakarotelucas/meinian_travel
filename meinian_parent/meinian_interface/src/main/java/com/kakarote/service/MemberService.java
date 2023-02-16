package com.kakarote.service;

import com.kakarote.pojo.Member;

/**
 * @Date: 2023/2/16 17:40
 * @Auther: Kakarotelu
 * @Description:
 */
public interface MemberService {
    //根据用户输入的手机号查询Member对应表中是否存在
    Member findByTelephone(String telephone);

    //新增会员
    void add(Member member);
}
