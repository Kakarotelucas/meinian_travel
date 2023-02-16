package com.kakarote.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.kakarote.dao.MemberDao;
import com.kakarote.pojo.Member;
import com.kakarote.service.MemberService;
import com.kakarote.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public void add(Member member) {
        //MD5加密
        if(member.getPassword() != null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }
}
