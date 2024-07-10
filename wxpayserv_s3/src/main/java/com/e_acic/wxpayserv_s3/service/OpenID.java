package com.e_acic.wxpayserv_s3.service;

import com.e_acic.wxpayserv_s3.entity.OrderInfo;
import com.e_acic.wxpayserv_s3.entity.UserInfo;
import com.e_acic.wxpayserv_s3.utils.ApplicationContextHelper;
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
import java.util.HashMap;
import java.util.Map;


@RestController
@Slf4j
public class OpenID{
    public OpenID() {

    }




    /*
   session_key	string	会话密钥
   unionid	string	用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台账号下会返回，详见 UnionID 机制说明。
   errmsg	string	错误信息
   openid	string	用户唯一标识
   errcode	int32	错误码
   description: 处理openid返回的数据，构造UserInfo entity,构造返回给小程序的json数据
     */


    public class WxOpenIDResp{
        public WxOpenIDResp() {
        }

        public WxOpenIDResp(String session_key, String unionid, String errmsg, String openid, Integer errcode) {
            this.session_key = session_key;
            this.unionid = unionid;
            this.errmsg = errmsg;
            this.openid = openid;
            this.errcode = errcode;
        }

        private static  Map<Integer,String> errMsgs = null;
        static {
            errMsgs = new HashMap<>();
            errMsgs.put(40029,"code无效");
            errMsgs.put(45011,"调用太频繁，请稍候再试");
            errMsgs.put(40226,"高风险等级用户，小程序登录拦截");
            errMsgs.put(-1,"系统繁忙，此时请开发者稍候再试");

        }


        private  String session_key;
        private String unionid;
        private String errmsg;
        private String openid;
        private Integer errcode;
        private String s3backurl;

        public String getS3backurl() {
            return s3backurl;
        }


        public void setS3backurl(String s3backurl) {
            this.s3backurl = s3backurl;
        }

        public String getSession_ley() {
            return session_key;
        }

        public void setSession_ley(String session_ley) {this.session_key = session_ley;}

        public String getUnionid() {

            return unionid;
        }

        public void setUnionid(String unionid) {this.unionid = unionid;}

        public String getErrmsg() {
            return errMsgs.get(errcode);
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public String getOpenid() {
            return this.openid;
        }


        public void setOpenid(String openid) {this.openid = openid;}

        public Integer getErrcode() {
            return errcode;
        }

        public void setErrcode(Integer errcode) {
            this.errcode = errcode;
        }

        public void encrpt(){

            this.session_key = Helper.getInstance().Encrypt(session_key);
            this.openid = Helper.getInstance().Encrypt(openid);
            this.unionid = Helper.getInstance().Encrypt(unionid);
            this.s3backurl = Helper.getInstance().Encrypt(s3backurl);
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
        public String getRespToWxApp(long in_time){
            String result = null;
            if (getErrcode() == 0){
                result = "{\"openid\":\""+ this.getOpenid()+"\",\"token\":\""+this.getUnionid()+"\",\"in_time\":"
                        +in_time+ ",\"code\":"+this.getErrcode()+",\"s3backurl\":\""+this.getS3backurl()+"\"}";
            }else {
                result = "{\"code\":"+this.getErrcode()+ ",\"failreason\":\""+this.getErrmsg()+"\"}";
            }
            return result;
        }
    }
    @Autowired
    private ConfigurableEnvironment enviroment;

    @Autowired
    private OpenIDAware openIDAware;

    /*
     * 传送openid，appid等信息给S3  返回支付链接并且把此链接返回给前段
     * 前端用此支付链接访问s3获取到支付参数
     * 前端使用支付参数拉起微信支付
     * */
    private String PostPayInfoToS3(String openid,String appid,String order){
        String result = "http3://www.s3url.com/getpayurl";
        /*String postoidurl = enviroment.getProperty("request.postoidurl");
        postoidurl += "?openid="+openid+"&appid="+appid+"&order="+order;
        result = Helper.getInstance().HttpsRequest(postoidurl,"get","");*/
        return result;
    }
    /*接收小程序发送的code(加密),向微信请求并返回加密的openid
     url: http://localhost:8080/wxpayserv_s3/getopenid?code=12222&token=22432423&order=3242342
     *
     */
    @RequestMapping("/getopenid")
    public String getOpenID(String code,String token,String order) {


        String result = null;
        //String decrptkey = ApplicationContextHelper.configurableEnvironment.getProperty("request.encrykey");
        String appid = Helper.getInstance().Decrypt(enviroment.getProperty("request.appid"));
//          初次登录，需要向微信服务器获取openid
        if ((code != null) && (code.length() >0))  {

            String secretKey = Helper.getInstance().Decrypt(enviroment.getProperty("request.secretcode"));
            String grantType = enviroment.getProperty("request.granttype");
            String openIDUrl = enviroment.getProperty("request.openidurl");
            String params = "?appid=" + appid + "&secret=" + secretKey + "&js_code=" + code + "&grant_type=" + grantType;
            openIDUrl += params;
            result = Instance.helper.HttpsRequest(openIDUrl,"get",null);
            if ((result != null) && (result.length() > 0)) {
                long ltime = System.currentTimeMillis();
                WxOpenIDResp wxOpenIDResp = new Gson().fromJson(result,WxOpenIDResp.class);
                //由于没有绑定到微信开放平台，小程序无法获取unionid, errcode, 调试模拟生成
                wxOpenIDResp.setErrcode(0);
                wxOpenIDResp.setUnionid(String.valueOf(System.currentTimeMillis()));

                if (wxOpenIDResp.getErrcode() == 0){  //微信返回code为0表正常获取
                    String s3backurl = PostPayInfoToS3(wxOpenIDResp.getOpenid(),appid,order);
                    wxOpenIDResp.setS3backurl(s3backurl);
                    wxOpenIDResp.encrpt();
                    result = wxOpenIDResp.getRespToWxApp(ltime);
                    try {
                        UserInfo userInfo = wxOpenIDResp.getUserEntity(new Timestamp(ltime));
                        openIDAware.CreateUser(userInfo);
                        openIDAware.CreateOrder(order,userInfo.getToken(),new Timestamp(System.currentTimeMillis()));
                    } catch (Exception e) {
                        log.error("获取openid后保存失败:"+e.toString());
                    }
                }else {
                    wxOpenIDResp.getRespToWxApp(ltime);
                }
            }
        } else if ((token != null) && (token.length() >0)) { //后台查询token

            try {
                UserInfo userInfo =  openIDAware.GetUserByToken(token);
                String openid = Helper.getInstance().Decrypt(userInfo.getOpenid());
                String s3backurl = PostPayInfoToS3(openid,appid,order);
                WxOpenIDResp wxOpenIDResp = new WxOpenIDResp();
                wxOpenIDResp.setOpenid(userInfo.getOpenid());
                wxOpenIDResp.setUnionid(userInfo.getToken());
                wxOpenIDResp.setErrcode(0);
                wxOpenIDResp.setS3backurl(Helper.getInstance().Encrypt(s3backurl));
                result = wxOpenIDResp.getRespToWxApp(userInfo.getIn_time().getTime());
                openIDAware.CreateOrder(order,token,new Timestamp(System.currentTimeMillis()));
            } catch (Exception e){
                log.error("查询到token,保存order信息失败:"+e.toString());
            }
        }

        return result;

    }



}
