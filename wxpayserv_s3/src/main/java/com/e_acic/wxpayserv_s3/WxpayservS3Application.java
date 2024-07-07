package com.e_acic.wxpayserv_s3;

import com.e_acic.wxpayserv_s3.service.UserProfile;
import com.e_acic.wxpayserv_s3.utils.ApplicationContextHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.sql.Timestamp;


@SpringBootApplication

public class WxpayservS3Application {

    public static void main(String[] args) {
        SpringApplication.run(WxpayservS3Application.class, args);


    }

}
