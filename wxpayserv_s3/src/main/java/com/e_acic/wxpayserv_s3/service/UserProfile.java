package com.e_acic.wxpayserv_s3.service;
/*
webpro
com.e_acic.wxpayserv_s3.service
@auth hdh
2024/7/7  6:10
description:
*/

import com.e_acic.wxpayserv_s3.utils.ApplicationContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        jdbcTemplate = ApplicationContextHelper.getBean(JdbcTemplate.class);
        Integer updata = jdbcTemplate.update("insert into wx.wx_user values(null,?,?,?)","32132133","213213213openid",new Timestamp(System.currentTimeMillis()));
        return  updata>0?true:false;
    }
}
