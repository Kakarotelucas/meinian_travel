package com.kakarote.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kakarote.constant.MessageConstant;
import com.kakarote.pojo.Setmeal;
import com.kakarote.results.Result;
import com.kakarote.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Date: 2023/2/11 17:33
 * @Auther: Kakarotelu
 * @Description:
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    private SetmealService setmealService;

    //移动端：获取所有套餐信息列表
    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){
        try {
            List<Setmeal> list = setmealService.findAll();
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }

    /**
     * 移动端：根据id获取单个套餐包括对应的跟团游和自由行
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        Setmeal setmeal = setmealService.findById(id);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

    /**
     * 移动端：只查询单个套餐对象
     * @param id
     * @return
     */
    @RequestMapping("/getSetmealById")
    public Result getSetmealById(Integer id){
        Setmeal setmeal = setmealService.getSetmealById(id);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }
}
