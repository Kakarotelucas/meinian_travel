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
    /*Page<TravelItem> findPage(String queryString);*/
    Page findPage(String queryString);

    //删除自由行
    void delete(Integer id);

    //编辑自由行回显数据
    TravelItem findById(Integer id);

    //编辑自由行表单内容
    void edit(TravelItem travelItem);
}

