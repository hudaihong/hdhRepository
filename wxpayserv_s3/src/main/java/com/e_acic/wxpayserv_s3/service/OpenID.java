package com.e_acic.wxpayserv_s3.service;

import com.e_acic.wxpayserv_s3.entity.OrderInfo;
import com.e_acic.wxpayserv_s3.entity.UserInfo;
import com.e_acic.wxpayserv_s3.utils.Helper;
import com.e_acic.wxpayserv_s3.utils.Instance;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;




@RestController
@Slf4j
public class OpenID{

    /*
   session_key	string	会话密钥
   unionid	string	用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台账号下会返回，详见 UnionID 机制说明。
   errmsg	string	错误信息
   openid	string	用户唯一标识
   errcode	int32	错误码
   description: 处理openid返回的数据，构造UserInfo entity,构造返回给小程序的json数据
     */

    public class WxOpenIDResp{
        private  String session_key;
        private String unionid;
        private String errmsg;
        private String openid;
        private Integer errcode;

        public WxOpenIDResp() {
        }

        public WxOpenIDResp(String session_ley, String unionid, String errmsg, String openid, Integer errcode) {
            this.session_key = session_ley;
            this.unionid = unionid;
            this.errmsg = errmsg;
            this.openid = openid;
            this.errcode = errcode;
        }

        public String getSession_ley() {
            return session_key;
        }

        public void setSession_ley(String session_ley) {
            this.session_key = session_ley;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public String getOpenid() {
            return openid;
        }
        @Value("request.encrykey")
        private String encrykey;
        public void setOpenid(String openid) {

            this.openid = Helper.getInstance().Encrypt(this.openid,encrykey);
        }

        public Integer getErrcode() {
            return errcode;
        }

        public void setErrcode(Integer errcode) {
            this.errcode = errcode;
        }

        /*
        处理数据库要保存的数据
        * */
        public UserInfo getUserEntity(Timestamp in_time){
            UserInfo userInfo = new UserInfo();
            userInfo.setOpenid(openid);
            userInfo.setToken(unionid);
            userInfo.setDeal_flag(false);
            userInfo.setIn_time(in_time);
            return userInfo;
        }

        public OrderInfo getOrderEntity(String order,Timestamp ordertime){
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setWx_order(order);
            orderInfo.setToken(this.getUnionid());
            orderInfo.setWx_ordertime(ordertime);
            orderInfo.setWx_paystatu(0);
            orderInfo.setWx_payfailreason("");
            return orderInfo;
        }

        /*
        处理向前端返回的json
        * */
        public String getRespToWxApp(Timestamp in_time){
            String result = "{\"openid\":\""+ this.openid+"\",\"token\":\""+this.getUnionid()+"\",\"in_time\":\""
                    +in_time.toString()+ "\"}";
            return result;
        }
    }
    @Autowired
    private ConfigurableEnvironment enviroment;

    @Autowired
    private OpenIDAware openIDAware;
    @RequestMapping("/getopenid")
    /*接收小程序发送的code(加密),向微信请求并返回加密的openid
     url: http://localhost:8080/wxpayserv_s3/getopenid?code=12222&token=22432423&order=3242342
     *
     */

    public String getOpenID(String code,String token,String order) {
        String result = null;
//          初次登录，需要向微信服务器获取openid
        if ((code != null) && (code.length() >0))  {
            String appid = enviroment.getProperty("request.appid");
            String secretKey = enviroment.getProperty("request.secretcode");
            String grantType = enviroment.getProperty("request.granttype");
            String openIDUrl = enviroment.getProperty("request.openidurl");
            String params = "?appid=" + appid + "&secret=" + secretKey + "&js_code=" + code + "&grant_type=" + grantType;
            openIDUrl += params;
            result = Instance.helper.HttpsRequest(openIDUrl,"get",null);
            if ((result != null) || (result.length() > 0)) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                WxOpenIDResp wxOpenIDResp = new Gson().fromJson(result,WxOpenIDResp.class);
                result = wxOpenIDResp.getRespToWxApp(timestamp);
                try {
                    UserInfo userInfo = wxOpenIDResp.getUserEntity(timestamp);
                    openIDAware.CreateUser(userInfo);
                    openIDAware.CreateOrder(order,userInfo.getToken(),new Timestamp(System.currentTimeMillis()));
                } catch (Exception e) {
                    log.error("获取openid后保存失败:"+e.toString());
                }
            }
        } else if ((token != null) || (token.length() >0)) { //后台查询token

            try {
                result =  openIDAware.GetUserByToken(token);
                openIDAware.CreateOrder(order,token,new Timestamp(System.currentTimeMillis()));
            } catch (Exception e){
                log.error("查询到token,保存order信息失败:"+e.toString());
            }
        }

        return result;

    }


    /*
    * 传送openid，appid等信息给S3  返回支付链接并且把此链接返回给前段
    * 前端用此支付链接访问s3获取到支付参数
    * 前端使用支付参数拉起微信支付
    * */
    private String PostPayInfoToS3(String openid,String appid,String order){
        String result = "";
        String postoidurl = enviroment.getProperty("request.postoidurl");
        postoidurl += "?openid="+openid+"&appid="+appid+"&order="+order;
        result = Helper.getInstance().HttpsRequest(postoidurl,"get","");
        return result;
    }
}
