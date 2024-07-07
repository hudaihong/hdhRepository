package com.e_acic.wxpayserv_s3.service;

import com.e_acic.wxpayserv_s3.bean.UserInfo;
import com.e_acic.wxpayserv_s3.utils.Helper;
import com.e_acic.wxpayserv_s3.utils.Instance;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;




@RestController
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
        public UserInfo getEntity(){
            UserInfo userInfo = new UserInfo();
            userInfo.setOpenid(openid);
            userInfo.setToken(unionid);
            userInfo.setDeal_flag(false);
            userInfo.setIn_time(new Timestamp(System.currentTimeMillis()));
            return userInfo;
        }


        /*
        处理向前端返回的json
        * */
        public String getRespToWxApp(){

            String encrptOpenID = Helper.getInstance().Encrypt(openid,encrykey);
            return null;
        }
    }
    @Autowired
    private ConfigurableEnvironment enviroment;


    @RequestMapping("/getopenid")
    //接收小程序发送的code(加密),向微信请求并返回加密的openid
    public String getOpenID(String code,String token) {
        String result = null;
//          初次登录，需要向微信服务器获取openid
        if ((code != null) && (code.length() >0))  {
            String appid = enviroment.getProperty("request.appid");
            String secretKey = enviroment.getProperty("request.secretcode");
            String grantType = enviroment.getProperty("request.granttype");
            String openIDUrl = enviroment.getProperty("request.openidurl");
            String params = "appid=" + appid + "&secret=" + secretKey + "&js_code=" + code + "&grant_type=" + grantType;
            result = Instance.helper.httpsRequest(openIDUrl,"get",null);
            if ((result != null) || (result.length() > 0)) {
                WxOpenIDResp wxOpenIDResp = new Gson().fromJson(result,WxOpenIDResp.class);
                result = wxOpenIDResp.getRespToWxApp();
            }
        } else if ((token != null) || (token.length() >0)) { //后台查询token

        }

        return result;
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
