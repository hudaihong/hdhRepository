package com.e_acic.wxpayserv_s3.service;
/*
webpro
com.e_acic.wxpayserv_s3.service
@auth hdh
2024/7/7  6:10
description:
*/

import com.e_acic.wxpayserv_s3.entity.OrderInfo;
import com.e_acic.wxpayserv_s3.entity.UserInfo;
import com.e_acic.wxpayserv_s3.utils.ApplicationContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class OpenIDAware {
    public OpenIDAware() {

    }

    @Autowired
    private  JdbcTemplate jdbcTemplate;
    /*
     * 错误码	错误码取值	解决方案
     40029	code 无效	js_code无效
     45011	api minute-quota reach limit  mustslower  retry next minute	API 调用太频繁，请稍候再试
     40226	code blocked	高风险等级用户，小程序登录拦截 。风险等级详见用户安全解方案
     -1	system error	系统繁忙，此时请开发者稍候再试
     * */

    private void init(){
        if (jdbcTemplate == null){
            jdbcTemplate = ApplicationContextHelper.applicationContext.getBean(JdbcTemplate.class);
        }
    }
    @Value("request.appid")
    private String appid;
    @Transactional
    public  Boolean CreateUser(String token, String openid, Date in_time) {
        //Integer updata = jdbcTemplate.update("insert into wx.wx_user values(null,?,?,?)",token,openid,in_time);

        try {
            init();
            Integer updata = jdbcTemplate.update("replace into wx.wx_user values(null,?,?,?)","32132133","213213213openid",new Timestamp(System.currentTimeMillis()));
            return  updata>0?true:false;
        } catch (BeansException e) {
            log.error(e.toString());
        }
        return false;
    }

    /*
    *

    存在则修改，不存在则新增
    mysql: replace into wx.wx_user values(null,?,?,?,?)
    返回如果有行被删除后增加，则返回大于1，如果只是增加，返回为1
    oracle : 存在则修改，不存在则新增
    merge into wx.wxuser t1
        using (select ? as token,? as openid,? as in_time,? as deal_flag  from dual) t2
        on ( t1.token = t2.token)
        when matched then
            update
            set t1.openid = t2.openid,t1.in_time = t2.in_time,t1.deal_flag=t2.deal_flag
        when not matched then
            insert (token,openid,in_time,deal_flag)
            values (t2.token,t2.openid,t2.in_time,t2.deal_flag);
     */
    @Transactional
    public Boolean CreateUser(UserInfo userInfo)throws Exception{
        init();
        Integer updata = jdbcTemplate.update("replace into wx.wx_user values(null,?,?,?,?)",userInfo.getToken(),userInfo.getOpenid(),userInfo.getIn_time(),userInfo.getDeal_flag());
        return  updata>0?true:false;
    }

    public UserInfo GetUserByToken(String token)throws Exception{
        String result = "";
        init();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select openid,in_time from wx.wx_user  where token=?",token);
        UserInfo userInfo = new UserInfo();
        while(sqlRowSet.next()) {
            userInfo.setOpenid(sqlRowSet.getString("openid"));
            userInfo.setToken(sqlRowSet.getString("token"));
            userInfo.setIn_time(sqlRowSet.getTimestamp("in_time"));

            /*result += "{\"openid\":\"" + sqlRowSet.getString("openid") + "\",\"in_time\":" + sqlRowSet.getTimestamp("in_time")
                    + ",\"appid\":\""+appid+"\"}";*/
        }
        return userInfo;
    }

    //初次请求openid传入的订单order
    @Transactional
    public Boolean CreateOrder(String order, String token,Timestamp ordertime)throws Exception{
        init();
        Integer update = jdbcTemplate.update("insert into wx.wx_order(token,wx_order,wx_ordertime,wx_paysuccess) values(?,?,?,false)",token,order,ordertime);
        return update > 0?true:false;
    }

    //支付成功哦吼更新订单信息
    @Transactional
    public Boolean UpdateOrder(String order,String token,Timestamp paytime,Integer paystatu,String failreason)throws Exception{
        init();
        Integer update = jdbcTemplate.update("update wx.wx_order set  wx_paytime=?,wx_paystatu=?,wx_payfailreason=? where token=? and wx_order=?",
                paytime,paystatu,failreason,token,order);
        return update>0?true:false;
    }

    @Transactional
    public Boolean UpdateOrder(OrderInfo orderInfo)throws Exception{
        init();
        Integer update = jdbcTemplate.update("update wx.wx_order set  wx_paytime=?,wx_paystatu=?,wx_payfailreason=? where token=? and wx_order=?",
                orderInfo.getWx_paytime(),orderInfo.getWx_paystatu(),orderInfo.getWx_payfailreason(),orderInfo.getToken(),orderInfo.getWx_order());
        return update>0?true:false;
    }

}
