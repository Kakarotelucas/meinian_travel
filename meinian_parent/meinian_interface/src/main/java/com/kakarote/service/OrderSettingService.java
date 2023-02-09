package com.kakarote.service;

import com.kakarote.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @Date: 2023/2/7 21:11
 * @Auther: Kakarotelu
 * @Description:
 */
public interface OrderSettingService {

    //上传模板文件
    void addBatch(List<OrderSetting> listData);

    //根据日期查询预约设置数据(获取指定日期所在月份的预约设置数据)
    List<Map> getOrderSettingByMonth(String date); //参数格式为：2023-02

    //根据指定日期修改可预约人数
    void editNumberByDate(OrderSetting orderSetting);
}
