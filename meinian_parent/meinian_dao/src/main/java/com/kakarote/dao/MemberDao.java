package com.kakarote.dao;

import com.kakarote.pojo.Member;

import java.util.List;

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

    //查询所有的会员数量
    int findMemberCountByMonth(String month);

    //（1）今日新增会员数
    int getTodayNewMember(String today);

    //（2）总会员数
    int getTotalMember();

    // （3）本周新增会员数（4）本月新增会员数
    int getThisWeekAndMonthNewMember(String weekMonday);


}
