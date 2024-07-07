package com.e_acic.wxpayserv_s3.utils;
/*
webpro
com.e_acic.wxpayserv_s3.utils
@auth hdh
2024/7/7  14:26
description:
*/

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHelper implements ApplicationContextAware {

    public static ApplicationContext applicationContext;
    public static ConfigurableEnvironment configurableEnvironment;
    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        applicationContext = arg0;
        configurableEnvironment = applicationContext.getBean(ConfigurableEnvironment.class);

    }
/*
    public static  <T> T getBean(Class<T> clsName) {
        T  object = applicationContext.getBean(clsName);
        return object;
    }*/
}
