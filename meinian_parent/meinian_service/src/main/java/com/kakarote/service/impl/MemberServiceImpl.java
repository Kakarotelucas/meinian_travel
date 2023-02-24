package com.kakarote.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.kakarote.dao.MemberDao;
import com.kakarote.pojo.Member;
import com.kakarote.service.MemberService;
import com.kakarote.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date: 2023/2/16 17:40
 * @Auther: Kakarotelu
 * @Description:
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    //根据用户输入的手机号查询Member对应表中是否存在
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.getMemberByTelephone(telephone);
    }

    //新增会员
    @Override
    public void add(Member member) {
        //MD5加密
        if(member.getPassword() != null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }


    //根据月份查询所有的会员数量
    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {
        List<Integer> list = new ArrayList<>();
        if(months != null && months.size() > 0){
            for (String month : months) {
                // 迭代过去12个月，每个月注册会员的数量，根据注册日期查询
                int count = memberDao.findMemberCountByMonth(month);
                list.add(count);
            }
        }
        return list;
    }


}
