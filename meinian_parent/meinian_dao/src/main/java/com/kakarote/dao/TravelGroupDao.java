package com.kakarote.dao;

import com.github.pagehelper.Page;
import com.kakarote.pojo.Setmeal;
import com.kakarote.pojo.TravelGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Date: 2023/1/31 20:05
 * @Auther: Kakarotelu
 * @Description:
 */
public interface TravelGroupDao {

    //组团游分页查询
    //使用@Param("queryString")注解可以在xml映射文件中填写queryString,替代 value
    Page<TravelGroup> findPage(@Param("queryString") String queryString);

    //新增跟团游
    void add(TravelGroup travelGroup);
    void setTravelGroupAndTravelItem(Map<String, Integer> map);

    //新增套餐中的回显所有跟团游
    List<TravelGroup> findAll();
}

