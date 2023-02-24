package com.kakarote.dao;

import com.github.pagehelper.Page;
import com.kakarote.pojo.Address;
import com.kakarote.pojo.Member;

import java.util.List;

/**
 * @Date: 2023/2/23 16:09
 * @Auther: Kakarotelu
 * @Description:
 */
public interface AddressDao {

    //获取所有分店地址
    List<Address> findAll();

    //分页查询分公司地址
    Page<Address> selectByCondition(String queryString);

    //新增分公司地址
    void addAddress(Address address);

    //删除分公司地址
    void deleteById(Integer id);

}
