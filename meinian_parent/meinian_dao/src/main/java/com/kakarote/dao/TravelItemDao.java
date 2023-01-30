package com.kakarote.dao;

import com.github.pagehelper.Page;
import com.kakarote.pojo.TravelItem;

/**
 * @Date: 2023/1/29 14:43
 * @Auther: Kakarotelu
 * @Description:
 */
public interface TravelItemDao {

    //新增自由行
    void add(TravelItem travelItem);

    //分页插件
    Page findPage(String queryString);

    /*Page<TravelItem> findPage(String queryString);*/


}

