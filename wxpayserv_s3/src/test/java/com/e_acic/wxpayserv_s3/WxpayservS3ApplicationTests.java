package com.e_acic.wxpayserv_s3;

import com.e_acic.wxpayserv_s3.service.OpenID;
import com.e_acic.wxpayserv_s3.utils.Helper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

@SpringBootTest
class WxpayservS3ApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("cesi");
    }
    @Test
    void constructResp()
    {
        String jsResp = "{\"openid\":\"232133131\",\"session_key\":\"rwrw3423234\",\"unionid\":\"9999888833\",\"errcode\":0,\"errmsg\":\"xxxxx\"}";
        Gson gson = new Gson();
        OpenID.WxOpenIDResp wxOpenIDResp = gson.fromJson(jsResp, OpenID.WxOpenIDResp.class);
        wxOpenIDResp.encrpt();
        String result = null;
        wxOpenIDResp.setErrcode(0);
        result = wxOpenIDResp.getRespToWxApp(new Timestamp(System.currentTimeMillis()));
        String decrpt = Helper.getInstance().Decrypt(wxOpenIDResp.getOpenid());
        wxOpenIDResp.setErrcode(-1);
        result = wxOpenIDResp.getRespToWxApp(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void encry1(){
        String s1 = Helper.getInstance().Encrypt("wx524f7715c25f1d55");
        String s2 = Helper.getInstance().Encrypt("0a275861b87f699645290812b7750f9a");

    }


}
