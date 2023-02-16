package com.kakarote.service;

import com.kakarote.pojo.Setmeal;
import com.kakarote.results.PageResult;
import java.util.List;


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

    //移动端查询所有套餐
    List<Setmeal> findAll();

    //根据id查询套餐信息(还要显示跟团游、自由行)
    Setmeal findById(int id);

    //只查询单个套餐对象
    Setmeal getSetmealById(int id);
}

