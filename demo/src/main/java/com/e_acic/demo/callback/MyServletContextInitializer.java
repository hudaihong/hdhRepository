package com.e_acic.demo.callback;
/*
webpro
com.e_acic.demo.callback
@auth hdh
2024/7/6  17:05
description:
*/

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyServletContextInitializer implements ServletContextInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("創建自定義servlet");
    }
}
