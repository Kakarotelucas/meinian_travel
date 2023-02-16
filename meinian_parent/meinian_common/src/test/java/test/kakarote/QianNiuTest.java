package test.kakarote;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

import java.awt.*;

/* 七牛云文件上传测试类
 * Zone.zone0:华东
 * Zone.zone1:华北
 * Zone.zone2:华南
 * 自动识别上传区域
 * Zone.autoZone
 */

public class QianNiuTest {
    //上传本地文件
    @Test
    public void uploadFile(){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());//华南
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "P3qjwKkJYIE_k602Vuor0CP8UEPjU-wrK4yqkLil";
        String secretKey = "_GEXLQM7UufsIl4XaMA9CdZlN3SbJJsm0toM7LDk";
        //空间名
        String bucket = "com.kakarote";
        //如果是Windows情况下，格式是 D:\\qiniu\\quartz.png，可支持中文
        String localFilePath = "D:/Desktop/picture/lyf.jpg";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);// Fu_sQTB6T2mQ3Ga2fZclfBzwY_cJ
            System.out.println(putRet.hash);// Fu_sQTB6T2mQ3Ga2fZclfBzwY_cJ
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    // 删除空间中的文件
    @Test
    public void deleteFile(){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        //...生成上传凭证，然后准备上传
        String accessKey = "P3qjwKkJYIE_k602Vuor0CP8UEPjU-wrK4yqkLil";
        String secretKey = "_GEXLQM7UufsIl4XaMA9CdZlN3SbJJsm0toM7LDk";
        //空间名
        String bucket = "com.kakarote";
        String key = "Fu_sQTB6T2mQ3Ga2fZclfBzwY_cJ";//文件哈希值名称
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }


}
