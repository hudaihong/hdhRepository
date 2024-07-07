package com.e_acic.demo;

import com.e_acic.demo.service.UserProfile;
import com.e_acic.demo.utils.ApplicationContextHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.naming.Context;
import java.sql.Timestamp;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);
        UserProfile userProfile = (UserProfile) ApplicationContextHelper.getBean(UserProfile.class);
        userProfile.CreateUser("test122","test22333",new Timestamp(System.currentTimeMillis()));
        System.out.println("啓動成功");


    }

}
