package com.kakarote.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.kakarote.constant.MessageConstant;
import com.kakarote.dao.MemberDao;
import com.kakarote.dao.OrderDao;
import com.kakarote.dao.OrderSettingDao;
import com.kakarote.pojo.Member;
import com.kakarote.pojo.Order;
import com.kakarote.pojo.OrderSetting;
import com.kakarote.results.Result;
import com.kakarote.service.OrderService;
import com.kakarote.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Date: 2023/2/14 13:11
 * @Auther: Kakarotelu
 * @Description: 提交预约信息表单
 */

@Service(interfaceClass =OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 接收预约用户信息
     * 1. 判断当前的日期是否可以预约(根据orderDate查询t_ordersetting, 能查询出来可以预约;查询不出来,不能预约)
     * 2. 判断当前日期是否预约已满(判断reservations（已经预约人数）是否等于number（最多预约人数）)
     * 3. 判断是否是会员(根据手机号码查询t_member)
     *     - 如果是会员(能够查询出来), 防止重复预约(根据member_id,orderDate,setmeal_id查询t_order)
     *     - 如果不是会员(不能够查询出来) ,自动注册为会员(直接向t_member插入一条记录)
     * 4.进行预约
     *       - 向t_order表插入一条记录
     *       - t_ordersetting表里面预约的人数reservations+1
     * @param map
     * @return
     */
    @Override
    public Result saveOrder(Map map) throws Exception {

        int setmealId = Integer.parseInt((String) map.get("setmealId"));
        // 1、判断当前的日期是否可以预约(根据orderDate查询t_ordersetting, 能查询出来可以预约;查询不出来,不能预约)
        String orderDate = (String) map.get("orderDate");
        //使用工具类将字符串转换成日期类型
        Date date = DateUtils.parseString2Date(orderDate);
        //通过日期查询，判断预约设置是否存在
        OrderSetting orderSetting = orderSettingDao.getOrderSettingByOrderDate(date);
        if (orderSetting == null){//预约设置不存在，不能预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }else{
            //预约设置已存在，可以预约。2、判断当前日期是否预约已满(判断reservations（已经预约人数）是否等于number（最多预约人数）)
            int reservations = orderSetting.getReservations();
            int number = orderSetting.getNumber();
            if (reservations >= number){//预约已满，不能预约
                return new Result(false,MessageConstant.ORDER_FULL);
            }
        }
        //3、预约未满，可以预约。预约时根据手机号判断是不是会员
        String telephone = (String) map.get("telephone");//用户输入的手机号
        //根据用户输入的手机号查询Member对应表中是否存在
        Member member = memberDao.getMemberByTelephone(telephone);
        if (member == null){
            //不是会员，自动注册为会员(直接向t_member插入一条记录)
            member = new Member();
            member.setName((String)map.get("name"));
            member.setSex((String)map.get("sex"));
            member.setIdCard((String)map.get("idCard"));
            member.setPhoneNumber((String)map.get("telephone"));
            member.setRegTime(new Date()); // 会员注册时间，当前时间
            //新增会员，需要进行主键回填
            memberDao.add(member);//需要进行主键回填
        }else{
            //是会员,检查是否重复预约(查询订单表中)，将findOrderCountByCondition封装成一个通用方法，可以根据查询条件不同实现动态查询SQL
            Integer memberId = member.getId();
            Order order = new Order(memberId, date, null, null, setmealId);
            //根据t_order表中查询会员id，下单时间，所订套餐id是否重复来判断是否重复预约
            List<Order> orderList = orderDao.findOrderByCondition(order);
            if (orderList != null && orderList.size() > 0){
                //已经重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }
        //4.是会员，未重复预约，此时进行预约
        //a.向t_order表插入一条记录
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(date);
        order.setOrderType(Order.ORDERTYPE_WEIXIN);
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setSetmealId(setmealId);
        //新增会员
        orderDao.add(order);//主键回填

        //b.可以预约，设置预约人数加一
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        //更新orderSetting预约设置表中预约人数的值
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }

    /**
     * 根据id查询预约信息，包括旅游人信息、套餐信息
     * @param id 订单id
     * @return
     */
    @Override
    public Map<String, Object> findById4Detail(Integer id) {
        Map<String, Object> map = orderDao.findById4Detail(id);
        if (map != null){
            try {
                //处理日期格式
                Date date = (Date) map.get("orderDate");
                map.put("orderDate", DateUtils.parseDate2String(date));
                return map;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return map;
    }
}
