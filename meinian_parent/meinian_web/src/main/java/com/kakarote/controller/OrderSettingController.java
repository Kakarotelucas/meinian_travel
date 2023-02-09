package com.kakarote.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kakarote.constant.MessageConstant;
import com.kakarote.pojo.OrderSetting;
import com.kakarote.results.Result;
import com.kakarote.service.OrderSettingService;
import com.kakarote.util.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Date: 2023/2/7 21:11
 * @Auther: Kakarotelu
 * @Description: 上传文件, 把编写好的Excel上传
 */
@RestController
@RequestMapping("/orderSetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 上传模板文件
     *
     * @param excelFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile) {
        try {
            // 1、使用poi工具类解析excel文件，读取里面的内容。把每行中的值作为一个数组，所有行作为一个集合返回
            List<String[]> lists = POIUtils.readExcel(excelFile);

            // 2、把List<String[]> 数据转换成 List<OrderSetting>数据
            List<OrderSetting> listData = new ArrayList<>();

            // 迭代里面的每一行数据，进行封装到集合里面
            for (String[] strArray : lists) {
                // 获取到一行（日期与可预约数量）里面，每个表格数据，进行封装
                String dataStr = strArray[0];
                String numberStr = strArray[1];
                OrderSetting orderSetting = new OrderSetting();
                //转换数据类型
                orderSetting.setOrderDate(new Date(dataStr));
                orderSetting.setNumber(Integer.parseInt(numberStr));
                /*给可预约数一个初始值,或者删除所有数据再上传模板执行更新操作即可使其为0
                orderSetting.setReservations(0);*/
                listData.add(orderSetting);
            }
            // 3、调用业务进行批量保存
            orderSettingService.addBatch(listData);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /*
     * 根据日期查询预约设置数据(获取指定日期所在月份的预约设置数据)
     * 前台需要的参数：用Map接收：
			this.leftobj = [
                        { date: 1, number: 120, reservations: 1 },
                        { date: 3, number: 120, reservations: 1 },
                        { date: 4, number: 120, reservations: 120 },
                        { date: 6, number: 120, reservations: 1 },
                        { date: 8, number: 120, reservations: 1 }
                    ];
     * @param date
     * @return
     */
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date) {//传来参数格式为：2023-02
        try {
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            //获取预约设置数据成功
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            //获取预约设置数据失败
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * 根据指定日期修改可预约人数
     * @param orderSetting
     * @return
     */
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
            //预约设置成功
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //预约设置失败
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
