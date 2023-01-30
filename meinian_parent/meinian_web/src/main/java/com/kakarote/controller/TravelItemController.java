package com.kakarote.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.kakarote.constant.MessageConstant;
import com.kakarote.results.PageResult;
import com.kakarote.results.QueryPageBean;
import com.kakarote.results.Result;
import com.kakarote.pojo.TravelItem;
import com.kakarote.service.TravelItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date: 2023/1/29 14:45
 * @Auther: Kakarotelu
 * @Description:
 */

@RestController //@Controller + @ResponseBody 需要使用@ResponseBody 注解，将转换后的JSON数据放入到响应体中
@RequestMapping("/travelItem")
public class TravelItemController {
    @Reference //远程调用服务
    TravelItemService travelItemService;
    /**
     * 新增自由行
     * @param travelItem
     * @return
     */
    @RequestMapping("add")
    //@RequestBody从请求体中取前端数据。formDate表单项参数名称与实体类对象属性名称要保持一致才可以封装
    public Result add(@RequestBody TravelItem travelItem){

        try {
            travelItemService.add(travelItem);
            return new Result(true, MessageConstant.ADD_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            return new Result(false,MessageConstant.ADD_TRAVELITEM_FAIL);
        }
    }

    /**
     * 分页查询
     * @param queryPageBean 用来接收前端数据
     * @return 返回分页对象
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult= travelItemService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());

        return pageResult;
    }

}
