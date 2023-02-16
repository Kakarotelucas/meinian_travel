package com.kakarote.dao;

import com.kakarote.pojo.Member;

/**
 * @Date: 2023/2/14 13:22
 * @Auther: Kakarotelu
 * @Description:
 */
public interface MemberDao {

    //根据用户输入的手机号查询Member对应表中是否存在
    Member getMemberByTelephone(String telephone);

    //新增会员，需要进行主键回填
    void add(Member member);

}
