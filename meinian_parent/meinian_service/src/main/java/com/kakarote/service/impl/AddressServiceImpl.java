package com.kakarote.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kakarote.dao.AddressDao;
import com.kakarote.pojo.Address;
import com.kakarote.results.PageResult;
import com.kakarote.results.QueryPageBean;
import com.kakarote.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Date: 2023/2/23 16:08
 * @Auther: Kakarotelu
 * @Description:
 */
@Service(interfaceClass = AddressService.class)
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;

    //获取所有分店地址
    @Override
    public List<Address> findAll() {
        return addressDao.findAll();
    }

    //分页查询分公司地址
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Address> page = addressDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    //新增分公司地址
    @Override
    public void addAddress(Address address) {
        addressDao.addAddress(address);
    }

    //删除分公司地址
    @Override
    public void deleteById(Integer id) {
        addressDao.deleteById(id);
    }

}

