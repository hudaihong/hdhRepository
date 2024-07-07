package com.e_acic.wxpayserv_s3.service;
/*
webpro
com.e_acic.wxpayserv_s3.service
@auth hdh
2024/7/7  6:10
description:
*/

import com.e_acic.wxpayserv_s3.bean.UserInfo;
import com.e_acic.wxpayserv_s3.utils.ApplicationContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Component
@Slf4j
public class UserProfile{
    @Autowired
    private  JdbcTemplate jdbcTemplate;
    @Transactional
    public  Boolean CreateUser(String token, String openid, Date in_time) {
        //Integer updata = jdbcTemplate.update("insert into wx.wx_user values(null,?,?,?)",token,openid,in_time);

        try {
            if (jdbcTemplate == null){
                jdbcTemplate = ApplicationContextHelper.applicationContext.getBean(JdbcTemplate.class);
            }
            Integer updata = jdbcTemplate.update("insert into wx.wx_user values(null,?,?,?)","32132133","213213213openid",new Timestamp(System.currentTimeMillis()));
            return  updata>0?true:false;
        } catch (BeansException e) {
            log.error(e.toString());
        }
        return false;
    }

    /*
    *  `token` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `openid` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `in_time` timestamp(6) NULL DEFAULT NULL,
  `deal_flag` tinyint DEFAULT '0' COMMENT '是否正在处理此用户的支付，为true表示正在处理',
    * */
    @Transactional
    public Boolean CreateUser(UserInfo userInfo){
        try {
            if (jdbcTemplate == null){
                jdbcTemplate = ApplicationContextHelper.applicationContext.getBean(JdbcTemplate.class);
                Integer updata = jdbcTemplate.update("insert into wx.wx_user values(null,?,?,?,?)",userInfo.getToken(),userInfo.getOpenid(),userInfo.getIn_time(),userInfo.getDeal_flag());
                return  updata>0?true:false;
            }
        } catch (BeansException e) {
            log.error(e.toString());
        }
        return false;
    }
}
