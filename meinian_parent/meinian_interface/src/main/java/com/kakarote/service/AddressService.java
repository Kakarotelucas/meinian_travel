package com.kakarote.service;

import com.kakarote.pojo.Address;
import com.kakarote.results.PageResult;
import com.kakarote.results.QueryPageBean;

import java.util.List;

/**
 * @Date: 2023/2/23 16:08
 * @Auther: Kakarotelu
 * @Description:
 */
public interface AddressService {

    //获取所有分店地址
    List<Address> findAll();

    //分页查询分公司地址
    PageResult findPage(QueryPageBean queryPageBean);

    //新增分公司地址
    void addAddress(Address address);

    //删除分公司地址
    void deleteById(Integer id);

}
