package com.e_acic.demo.service;

/*
webpro
com.e_acic.demo.service
@auth  hdh
description:
2024-07-05 15:58
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestService {

    @Autowired
    private JdbcTemplateAutoConfiguration jdbcTemplateAutoConfiguration;
    @RequestMapping("/getopenid")
    public String getOpenID(){
        return "this is a 中文";
    }
}
