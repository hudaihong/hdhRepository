package com.e_acic.wxpayserv_s3;

import com.e_acic.wxpayserv_s3.bean.UserInfo;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/*
webpro
com.e_acic.wxpayserv_s3
@auth  hdh
description:
2024-07-05 9:45
*/
@Slf4j
public class Test {
    private JdbcTemplate jdbcTemplate;
    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName("12");

    }
}
