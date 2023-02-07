package com.kakarote.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kakarote.dao.TravelItemDao;
import com.kakarote.results.PageResult;
import com.kakarote.pojo.TravelItem;
import com.kakarote.results.Result;
import com.kakarote.service.TravelItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Date: 2023/1/29 14:59
 * @Auther: Kakarotelu
 * @Description:
 */
@Service(interfaceClass = TravelItemService.class) //发布服务，注册到zk中心
@Transactional //声明式事务，所有的方法都加上事务
public class TravelItemServiceImpl implements TravelItemService {

    @Autowired
    TravelItemDao travelItemDao;

    //新增自由行
    @Override
    public void add(TravelItem travelItem) {
        travelItemDao.add(travelItem);
    }

    //分页查询
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 不使用分页插件PageHelper
        // 至少写2条sql语句完成查询
        // 第1条，select count(*) from t_travelitem，查询的结果封装到PageResult中的total
        // 第2条，select * from t_travelitem where NAME = '001' OR CODE = '001' limit ?,?
        //（0,10）（10,10）(（currentPage-1)*pageSize,pageSize）
        // 使用分页插件PageHelper（简化上面的操作）
        //开启分页功能，索引计算：limit（currentPage - 1）*pageSize，pageSize
        PageHelper.startPage(currentPage,pageSize);
        // 2：使用sql语句进行查询（不必在使用mysql的limit了）
        Page page = travelItemDao.findPage(queryString); //返回当前页数据
        // 3：封装
        return new PageResult(page.getTotal(), page.getResult());//返回总记录数与分页数据集合（当前页数据）

        /*Page<TravelItem> page = travelItemDao.findPage(queryString);
        // 3：封装
        return new PageResult(page.getTotal(),page.getResult());*/

    }

    /**
     * //删除自由行
     * @param id
     */
    @Override
    public void delete(Integer id) {
        //查询自由行关联表表中是否关联其他数据，如果存在，就抛异常，不进行删除
        Long conunt = travelItemDao.findCountByTravelitemId(id);
        if (conunt > 0){
            //有关联数据
            throw new RuntimeException("删除自由行失败！存在关联数据不允许删除！");
        }
        travelItemDao.delete(id);
    }



    /**
     * 编辑自由行回显数据
     * @param id
     * @return
     */
    @Override
    public TravelItem findById(Integer id) {
        TravelItem travelItem = travelItemDao.findById(id);
        return travelItem;
    }

    /**
     *编辑自由行表单内容
     * @param travelItem
     */
    @Override
    public void edit(TravelItem travelItem) {
        travelItemDao.edit(travelItem);
    }

    /**
     * //新增跟团游中回显自由行信息
     * @return
     */
    @Override
    public List<TravelItem> findAll() {
        return travelItemDao.findAll();
    }
}
