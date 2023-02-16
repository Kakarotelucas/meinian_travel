package com.kakarote.dao;

import com.github.pagehelper.Page;
import com.kakarote.pojo.TravelItem;

import java.util.List;

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

    //报团游新增出查询自由行列表
    List<TravelItem> findAll();

    //删除自由行进行关联校验
    Long findCountByTravelitemId(Integer id);

    //帮助跟团游查询关联数据的方法。id是跟团游传来的id
    List<TravelItem> findTravelItemListById(Integer id);
}

