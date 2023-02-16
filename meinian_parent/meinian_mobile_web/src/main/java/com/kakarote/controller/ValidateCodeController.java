package com.kakarote.controller;

import com.aliyuncs.exceptions.ClientException;
import com.kakarote.constant.MessageConstant;
import com.kakarote.constant.RedisMessageConstant;
import com.kakarote.results.Result;
import com.kakarote.util.SMSUtils;
import com.kakarote.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @Date: 2023/2/14 10:00
 * @Auther: Kakarotelu
 * @Description: 提供方法发送短信验证码，并将验证码保存到redis
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    //预约时发送手机验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        Integer code = ValidateCodeUtils.generateValidateCode(4);//生成4位数字验证码
        try {
            //发送短信
            SMSUtils.sendShortMessage(telephone, code.toString());
        } catch (Exception e) {
            e.printStackTrace();
            //验证码发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为：" + code);
        //设置存储时间：将生成的验证码缓存到redis，5 * 60表示秒，共5分钟。表示验证码五分钟有效
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5 * 60, code.toString());
        //验证码发送成功
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    //手机快速登录时发送手机验证码
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        Integer code = ValidateCodeUtils.generateValidateCode(4);//生成4位数字验证码
        try {
            //发送短信
            SMSUtils.sendShortMessage(telephone, code.toString());
        } catch (Exception e) {
            e.printStackTrace();
            //验证码发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为：" + code);
        //将生成的验证码缓存到redis。有效时间为五分钟
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 5 * 60, code.toString());
        //验证码发送成功
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

}

