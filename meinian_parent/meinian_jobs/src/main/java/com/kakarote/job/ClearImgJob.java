package com.kakarote.job;

import com.kakarote.constant.RedisConstant;
import com.kakarote.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

/**
 * @Date: 2023/2/7 03:16
 * @Auther: Kakarotelu
 * @Description: 定时清理图片
 */
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    //清理图片
    public void clearImg(){
        Jedis resource = jedisPool.getResource();
        //计算redis中两个集合的差值，获取垃圾图片名称
        //需要注意：在比较的时候，数据多的放到前边，如果pic多，那么pic放到前面，db多，db放到前面
        //在这里，是用pic数据集合减去db的数据集合，相同名字保留，不同名字的就说明没在数据库保存，
        //在pic中、云服务中删除 SDIFF key1 [key2]返回给定所有集合的差集
        Set<String> set = resource.sdiff(
                RedisConstant.SETMEAL_PIC_RESOURCES,
                RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //迭代差集后的set集合
        Iterator<String> iterator = set.iterator();
        //从0开始，判断集合中是否有下一个数据，有就删除
        while (iterator.hasNext()){
            String next = iterator.next();
            System.out.println("删除图片的名称是：" + next);
            //删除图片服务器中的图片文件
            QiniuUtils.deleteFileFromQiniu(next);
            //删除redis中的数据
            resource.srem(RedisConstant.SETMEAL_PIC_RESOURCES,next);
        }
        resource.close();
    }
}

