package com.kakarote.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kakarote.constant.MessageConstant;
import com.kakarote.constant.RedisMessageConstant;
import com.kakarote.results.Result;
import com.kakarote.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

/**
 * @Date: 2023/2/14 13:09
 * @Auther: Kakarotelu
 * @Description: 旅游预约
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 页面表单数据来源于多张表，pojo接收不完整，用Map接收表单提交的预约用户信息
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map){
        /*map中的数据：
                idCard: "431251200012080021"
                name: "张三"
                orderDate: "2023-02-16"
                setmealId: "3"
                sex: "1"
                telephone: "15581472888"
                validateCode: "2222"
           */
        try {
            System.out.println("map = " + map);
            //从map中获得手机号和验证码
            String telephone = (String) map.get("telephone");
            String validateCode = (String) map.get("validateCode");

            //从Redis中获取缓存的验证码，key为手机号+RedisConstant.SENDTYPE_ORDER
            String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);

            //校验手机验证码value = {char[11]@7894} [1, 5, 5, 8, 1, 4, 7, 3, 0, 0, 0]
            if (!codeInRedis.equals(validateCode) | codeInRedis == null){
                return new Result(false,MessageConstant.VALIDATECODE_ERROR);
            }

            //手机验证码验证成功，调用业务逻辑完成订单信息的保存
            Result result = orderService.saveOrder(map);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_FAIL);
        }
    }

    /**根据id查询预约信息，包括旅游人信息、套餐信息
     *<p>会员姓名：{{orderInfo.member}}</p>
     *<p>旅游套餐：{{orderInfo.setmeal}}</p>
     *<p>旅游日期：{{orderInfo.orderDate}}</p>
     *<p>预约类型：{{orderInfo.orderType}}</p>
     * @param id 订单id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map<String, Object> map = null;
            map = orderService.findById4Detail(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
