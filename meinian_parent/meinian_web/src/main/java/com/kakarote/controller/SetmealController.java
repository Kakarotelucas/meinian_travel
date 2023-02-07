package com.kakarote.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kakarote.constant.MessageConstant;
import com.kakarote.constant.RedisConstant;
import com.kakarote.pojo.Setmeal;
import com.kakarote.results.PageResult;
import com.kakarote.results.QueryPageBean;
import com.kakarote.results.Result;
import com.kakarote.service.SetmealService;
import com.kakarote.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * @Date: 2023/2/2 20:22
 * @Auther: Kakarotelu
 * @Description:
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    //@Reference切记不能漏
    @Reference
    SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;
    /**
     * 上传图片
     *
     * @param imgFile
     * @return
     */
    @RequestMapping("/upload")
    //@RequestParam("imgFile")参数加上后以参数为准，
    //MultipartFile是SpringMVC提供简化上传操作的工具类，在spring配置文件中配置文件上传组件。
    //imgFile:需要跟页面el-upload里面的name保持一致

    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        try {
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            // 做字符串的截取。找到.最后出现的位置
            int lastIndexOf = originalFilename.lastIndexOf(".");//lyf.jpg
            //获取文件后缀,从找到.最后出现的位置开始截取
            String suffix = originalFilename.substring(lastIndexOf); //得到.jpg
            //使用UUID随机产生文件名称，防止同名文件覆盖
            String fileName = UUID.randomUUID().toString() + suffix;
            //使用工具类上传
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            //图片上传成功，返回文件名用于前端回显数据
            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);

            //*********Redis定时删除补充代码***********
            //将上传图片名称存入Redis，基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            //**************************************

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 新增套餐
     *
     * @param setmeal
     * @param travelgroupIds
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] travelgroupIds) {
       /* try {
            setmealService.add(setmeal,travelgroupIds);
            //新增套餐成功
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        }catch (Exception e){
            //新增套餐失败
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }*/
        try {
            setmealService.add(setmeal, travelgroupIds);
        } catch (Exception e) {
            //新增套餐失败
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        //新增套餐成功
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = setmealService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }
}


