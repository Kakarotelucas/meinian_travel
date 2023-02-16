package com.kakarote.dao;
import com.kakarote.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Date: 2023/2/7 21:12
 * @Auther: Kakarotelu
 * @Description:
 */
public interface OrderSettingDao {

    //保存数据到表格中（根据指定日期修改可预约人数）
    void add(OrderSetting orderSetting);

    //查询当前预约日期预约数量是否不止一个
    int findCountByOrderDate(Date orderDate);

    //设置预约日期，更新number数量
    void editNumberByOrderDate(OrderSetting orderSetting);

    //根据日期查询预约设置数据(获取指定日期所在月份的预约设置数据)
    List<OrderSetting> getOrderSettingByMonth(Map<String, Object> map);

    //通过日期查询，判断预约设置是否存在
    OrderSetting getOrderSettingByOrderDate(Date date);

    //更新orderSetting预约设置表中预约人数的值
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
