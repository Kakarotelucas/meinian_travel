package com.kakarote.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.kakarote.dao.OrderSettingDao;
import com.kakarote.pojo.OrderSetting;
import com.kakarote.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date: 2023/2/7 21:11
 * @Auther: Kakarotelu
 * @Description:
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 上传模板文件
     * @param listData
     */

    @Override
    public void addBatch(List<OrderSetting> listData) {
        // 1：遍历List<OrderSetting>
        for (OrderSetting orderSetting : listData) {
            // 1、判断当前的日期之前是否已经被设置过预约日期，使用当前时间作为条件查询数量,
            int count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
            // 2、如果设置过预约日期，更新number数量
            if (count>0){
                //预约设置存在了
                orderSettingDao.editNumberByOrderDate(orderSetting);
            }else {
                // 如果没有设置过预约日期，执行保存
                orderSettingDao.add(orderSetting);
            }
        }
    }

    /**
     * 根据日期查询预约设置数据(获取指定日期所在月份的预约设置数据)
     * @param date
     * @return
     * 传递的参数
     * date（格式：2023-1）
     *构建的数据List<Map>
     *  map.put("date",1);
     *  map.put("number",120);
     *  map.put("reservations",10);
     *查询方案：SELECT * FROM t_ordersetting WHERE orderDate LIKE '2019-08-%'
     *查询方案：SELECT * FROM t_ordersetting WHERE orderDate BETWEEN '2019-9-1' AND '2019-9-31'
     */
    @Override
    //根据日期查询预约设置数据
    public List<Map> getOrderSettingByMonth(String date) {//2023-2
        // 1.封装查询Map，dateBegin表示月份开始时间，dateEnd月份结束时间
        String dateBegin = date + "-1";//2023-2-1
        String dateEnd = date + "-31";//2023-2-31
        Map<String,Object> map = new HashMap();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);
        // 2.查询当前月份的预约设置
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> data = new ArrayList<>();
        // 3.将List<OrderSetting>，封装成List<Map>
        for (OrderSetting orderSetting : list) {
            Map orderSettingMap = new HashMap();
            orderSettingMap.put("date",orderSetting.getOrderDate().getDate());//获得日期（几号）
            orderSettingMap.put("number",orderSetting.getNumber());//可预约人数
            orderSettingMap.put("reservations",orderSetting.getReservations());//已预约人数
            data.add(orderSettingMap);
        }
        return data;
    }

    /**
     * 根据指定日期修改可预约人数
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        int count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if(count > 0){
            //当前日期已经进行了预约设置，需要进行修改操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else{
            //当前日期没有进行预约设置，进行添加操作
            orderSettingDao.add(orderSetting);
        }
    }

}
