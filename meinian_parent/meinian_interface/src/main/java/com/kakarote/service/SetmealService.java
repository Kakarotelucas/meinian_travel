package com.kakarote.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.kakarote.pojo.Setmeal;
import com.kakarote.results.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @Date: 2023/2/2 20:23
 * @Auther: Kakarotelu
 * @Description:
 */
public interface SetmealService {

    //新增套餐
    void add(Setmeal setmeal, Integer[] travelgroupIds);

    //分页查询
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
}
