package com.lijian.game.thirdparty.component;


import com.lijian.game.thirdparty.util.HttpUtils;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//@ConfigurationProperties(prefix = "spring.alicloud.sms")
@Component
@Data
public class SmsComponent {


    public  void sendSmsCode(String phone, String code){
        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        String appcode = "53fc5b1c5f5a4e4495040c5953894032";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        querys.put("param", "**code**:"+code+",**minute**:5");
        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
