package com.kakarote.service;


import com.kakarote.results.PageResult;
import com.kakarote.pojo.TravelItem;

import java.util.List;

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

    //删除自由行
    void delete(Integer id);

    //编辑自由行回显数据
    TravelItem findById(Integer id);

    //编辑更改自由行表单内容
    void edit(TravelItem travelItem);

    //新增跟团游中回显自由行信息
    List<TravelItem> findAll();
}

