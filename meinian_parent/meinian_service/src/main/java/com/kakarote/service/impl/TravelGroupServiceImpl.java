package com.kakarote.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kakarote.dao.TravelGroupDao;
import com.kakarote.pojo.TravelGroup;
import com.kakarote.results.PageResult;
import com.kakarote.service.TravelGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date: 2023/1/31 20:03
 * @Auther: Kakarotelu
 * @Description:
 */
@Service(interfaceClass = TravelGroupService.class)
@Transactional
public class TravelGroupServiceImpl implements TravelGroupService {

    @Autowired
    TravelGroupDao travelGroupDao;

    /**
     * 新增跟团游
     * @param travelGroup
     * @param travelItemIds
     */
    @Override
    public void add(TravelGroup travelGroup, Integer[] travelItemIds) {
        //1.新增跟团游，向t_travelgroup中添加数据，新增后返回新增的id
        travelGroupDao.add(travelGroup);
        //2.新增跟团游和自由行中间表t_travelgroup_travelitem新增数据(新增几条，由travelItemIds决定)
        setTravelGroupAndTravelItem(travelGroup.getId(), travelItemIds);
    }

    private void setTravelGroupAndTravelItem(Integer travelGroupId, Integer[] travelItemIds) {
        //新增几条数据，由travelItemIds决定,当travelItemIds非空时才进行循环
        if (travelItemIds != null && travelItemIds.length > 0){
            for (Integer travelItemId : travelItemIds) {
                //使用Map将它们封装，会将key传到Dao.xml中
                Map<String, Integer> map = new HashMap<>();
                map.put("travelGroup", travelGroupId);
                map.put("travelItem", travelItemId);
                travelGroupDao.setTravelGroupAndTravelItem(map);
            }
        }

    }

    /**
     * 组团游分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 使用分页插件PageHelper，设置当前页，每页最多显示的记录数
        PageHelper.startPage(currentPage,pageSize);
        // 响应分页插件的Page对象
        Page<TravelGroup> page = travelGroupDao.findPage(queryString);
        // 封装 page.getResult()就是分页数据集合
        return new PageResult(page.getTotal(), page.getResult());//返回总记录数与分页数据集合（当前页数据）
    }


    /**
     * 新增套餐中的回显所有跟团游
     * @return
     */
    @Override
    public List<TravelGroup> findAll() {
        return travelGroupDao.findAll();
    }

}
