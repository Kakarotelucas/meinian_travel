package com.kakarote.dao;

import com.kakarote.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * @Date: 2023/2/14 13:15
 * @Auther: Kakarotelu
 * @Description:
 */
public interface OrderDao {

    //根据t_order表中查询会员id，下单时间，所订套餐id是否重复来判断是否重复预约
    List<Order> findOrderByCondition(Order orde);

    //新增会员
    void add(Order order);

    //根据订单id查询预约信息，包括旅游人信息、套餐信息
    Map findById4Detail(Integer id);
}
