package com.kakarote.dao;

import com.github.pagehelper.Page;
import com.kakarote.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @Date: 2023/2/2 20:31
 * @Auther: Kakarotelu
 * @Description:
 */
public interface SetmealDao {

    /**
     * 新增套餐
     * @param setmeal
     */
    void add(Setmeal setmeal);
    void setSetmealAndTravelGroup(Map<String, Integer> map);

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<Setmeal> findPage(String queryString);

    //移动端查询所有套餐
    List<Setmeal> findAll();

    //根据id查询套餐信息(还要显示跟团游、自由行)
    Setmeal findById(int id);

    //只查询单个套餐对象
    Setmeal getSetmealById(int id);

    //统计套餐预约人数占比（饼图）
    List<Map<String, Object>> findSetmealCount();
}
