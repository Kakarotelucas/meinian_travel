package com.kakarote.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kakarote.constant.RedisConstant;
import com.kakarote.dao.SetmealDao;
import com.kakarote.pojo.Setmeal;
import com.kakarote.results.PageResult;
import com.kakarote.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

/**
 * @Date: 2023/2/2 20:24
 * @Auther: Kakarotelu
 * @Description:
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 新增套餐
     * @param setmeal
     * @param travelgroupId
     */
    @Override
    public void add(Setmeal setmeal, Integer[] travelgroupId) {
        //新增套餐
        setmealDao.add(setmeal);
        //向套餐和跟团游的中间表中插入数据
        if (travelgroupId != null && travelgroupId.length > 0){
            //绑定套餐和跟团游的多对多关系
            setSetmealAndTravelGroup(setmeal.getId(), travelgroupId);
        }
        //************************补充代码：将图片名称保存到Redis
        savePic2Redis(setmeal.getImg());
    }

    //将图片名称保存到Redis
    private void savePic2Redis(String pic){
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }



    //绑定套餐和跟团游的多对多关系方法
    private void setSetmealAndTravelGroup(Integer setmealId, Integer[] travelgroupId) {
        for (Integer checkgroupId  : travelgroupId) {
            Map<String, Integer> map = new HashMap<>();
            map.put("travelgroup_id",checkgroupId);
            map.put("setmeal_id",setmealId);
            setmealDao.setSetmealAndTravelGroup(map);
        }
    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }
}
