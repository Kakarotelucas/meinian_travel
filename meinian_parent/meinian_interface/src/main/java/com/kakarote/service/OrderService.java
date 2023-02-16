package com.kakarote.service;

import com.kakarote.results.Result;

import java.util.Map;

/**
 * @Date: 2023/2/14 13:11
 * @Auther: Kakarotelu
 * @Description:
 */
//接收预约用户信息
public interface OrderService {

    //用Map接收表单提交的预约用户信息
    Result saveOrder(Map map) throws Exception;

    //根据id查询预约信息，包括旅游人信息、套餐信息
    Map<String, Object> findById4Detail(Integer id);
}
