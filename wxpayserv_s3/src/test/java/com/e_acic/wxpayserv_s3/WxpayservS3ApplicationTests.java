package com.e_acic.wxpayserv_s3;

import com.e_acic.wxpayserv_s3.entity.OrderInfo;
import com.e_acic.wxpayserv_s3.entity.UserInfo;
import com.e_acic.wxpayserv_s3.service.OpenID;
import com.e_acic.wxpayserv_s3.service.OpenIDAware;
import com.e_acic.wxpayserv_s3.utils.Helper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private  OpenIDAware openIDAware;
    @Test
    void inputUser(){
        UserInfo userInfo = new UserInfo();
        userInfo.setToken("321321321321321321=====");
        userInfo.setOpenid("fhsklfjdfs;11===???");
        userInfo.setIn_time(new Timestamp(System.currentTimeMillis()));
        userInfo.setDeal_flag(false);

        try {
            openIDAware.CreateUser(userInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void inputOrder(){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setWx_order("34423423432");
        orderInfo.setWx_paystatu(-1);
        orderInfo.setWx_ordertime(new Timestamp(System.currentTimeMillis()));
        orderInfo.setWx_paytime(new Timestamp(System.currentTimeMillis()));
        orderInfo.setWx_payfailreason("高位用户，禁止使用微信");
        orderInfo.setToken("321321321321321321=====");

        try {
            openIDAware.CreateOrder(orderInfo.getWx_order(),orderInfo.getToken(),orderInfo.getWx_ordertime());
            System.out.println("更新order");
            openIDAware.UpdateOrder(orderInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


}
