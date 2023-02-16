package test.kakarote;

import com.kakarote.util.HttpUtils;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @Date: 2023/2/13 05:11
 * @Auther: Kakarotelu
 * @Description: 山东鼎信验证码测试
 */
public class SMSTest {
    public static void main(String[] args) {
        String host = "http://dingxin.market.alicloudapi.com"; //固定地址
        String path = "/dx/sendSms"; //固定映射地址
        String method = "POST";
        String appcode = "5e031c96afb5498aa9413a0220af5d1a";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", "155xxxx0305");
        querys.put("param", "code:6666");
        querys.put("tpl_id", "TP1711063");//测试模板
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
