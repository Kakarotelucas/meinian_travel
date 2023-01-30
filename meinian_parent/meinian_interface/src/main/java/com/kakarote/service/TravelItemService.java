package com.kakarote.service;


import com.kakarote.results.PageResult;
import com.kakarote.pojo.TravelItem;

/**
 * @Date: 2023/1/29 14:51
 * @Auther: Kakarotelu
 * @Description:
 */
//ctrl + alt + b 可以跳转到其实现类
public interface TravelItemService {

    //新增自由行
    void add(TravelItem travelItem);

    //分页查询
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
}


