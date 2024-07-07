package com.e_acic.wxpayserv_s3.service;
/*
webpro
com.e_acic.wxpayserv_s3.service
@auth hdh
2024/7/7  6:10
description:
*/

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserProfile implements IUserProfile{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Transactional
    @Override
    public Integer CreateUser(String name,String pwd) {
        Integer updata = jdbcTemplate.update("insert into table values(?,?)",name,pwd);

        if ((updata > 0)) {
            log.info("succsess");

        } else {
            log.warn("fail");
        }
        return updata;
    }
}
