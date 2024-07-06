package com.e_acic.wxpayserv_s3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OpenID {
    /*@Value("${wxrequest.appid}")
    private String appid;
    @Value("${server.port}")
    private String port;
*/

    @Autowired
    private ConfigurableEnvironment enviroment;




    @RequestMapping("/getopenid")
    //接收小程序发送的code(加密),向微信请求并返回加密的openid
    public String getOpenID(String code) {


//        String appId = appid;
        String appid = enviroment.getProperty("request.appid");
        String secretKey = enviroment.getProperty("request.secretcode");
        String grantType = enviroment.getProperty("request.granttype");
        String openIDUrl = enviroment.getProperty("request.openidurl");
        String params = "appid=" + appid + "&secret=" + secretKey + "&js_code=" + code + "&grant_type=" + grantType;
        return params;
        /*   String sr = req.sendGet(openIDUrl, params);
        JSONObject json = JSONObject.fromObject(sr);

// getting session_key
        String sessionKey = json.get("session_key").toString();

// getting open_id
        String openId = json.get("openid").toString();

// decoding encrypted info with AES
        try {
            String result = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
            if (null != result && result.length() > 0) {
                map.put("status", 1);
                map.put("msg", "解密成功");

                JSONObject userInfoJSON = JSONObject.fromObject(result);
                Map userInfo = new HashMap();
                userInfo.put("openId", userInfoJSON.get("openId"));
                userInfo.put("nickName", userInfoJSON.get("nickName"));
                userInfo.put("gender", userInfoJSON.get("gender"));
                userInfo.put("city", userInfoJSON.get("city"));
                userInfo.put("province", userInfoJSON.get("province"));
                userInfo.put("country", userInfoJSON.get("country"));
                userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
                userInfo.put("unionId", userInfoJSON.get("unionId"));
                map.put("userInfo", userInfo);
                return map;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("status", 0);
        map.put("msg", "解密失败");
        return map;
        */

    }
}
