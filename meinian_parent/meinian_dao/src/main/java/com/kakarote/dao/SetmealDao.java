package com.kakarote.dao;

import com.github.pagehelper.Page;
import com.kakarote.pojo.Setmeal;

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
}
