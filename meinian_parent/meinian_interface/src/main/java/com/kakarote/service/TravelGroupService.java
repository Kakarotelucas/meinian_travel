package com.kakarote.service;

import com.kakarote.pojo.TravelGroup;
import com.kakarote.pojo.TravelItem;
import com.kakarote.results.PageResult;
import com.kakarote.results.Result;

import java.util.List;

/**
 * @Date: 2023/1/31 20:03
 * @Auther: Kakarotelu
 * @Description:
 */
public interface TravelGroupService {

    //新增跟团游
    void add(TravelGroup travelGroup, Integer[] travelItemIds);

    //组团游分页查询
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);




    //新增套餐中的回显所有跟团游
    List<TravelGroup> findAll();
}
