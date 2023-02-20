package com.kakarote.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.kakarote.constant.MessageConstant;
import com.kakarote.results.PageResult;
import com.kakarote.results.QueryPageBean;
import com.kakarote.results.Result;
import com.kakarote.pojo.TravelItem;
import com.kakarote.service.TravelItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @PreAuthorize("hasAuthority('TRAVELITEM_ADD')")//权限校验
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
    @PreAuthorize("hasAuthority('TRAVELITEM_QUERY')")//权限校验
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = travelItemService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }


    /**
     * //删除自由行
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('TRAVELITEM_DELETE')")//权限校验，使用TRAVELITEM_DELETE123测试
    @RequestMapping("/delete")
    public Result delete(Integer id){ //可以使用 RequestParam("id")接收
        try {
            travelItemService.delete(id);
            return new Result(true, MessageConstant.DELETE_TRAVELITEM_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            //取异常消息
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_TRAVELITEM_FAIL);
        }
    }

    /**
     * 编辑表单回显数据
     * @param id
     * @return 注意travelItem别漏
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){

        try {
            TravelItem travelItem = travelItemService.findById(id);
            return new Result(true, MessageConstant.QUERY_TRAVELITEM_SUCCESS,travelItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.QUERY_TRAVELITEM_FAIL);
        }
    }

    /**
     * 编辑自由行表单内容
     * @param travelItem
     * @return
     */
    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('TRAVELITEM_EDIT')")//权限校验
    public Result edit(@RequestBody TravelItem travelItem){
        travelItemService.edit(travelItem);
        return new Result(true,MessageConstant.EDIT_TRAVELITEM_SUCCESS);
    }

    /**
     * 新增跟团游中回显自由行信息
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        List<TravelItem> lists =  travelItemService.findAll();
        return new Result(true, MessageConstant.QUERY_TRAVELGROUP_SUCCESS,lists);
    }

}
