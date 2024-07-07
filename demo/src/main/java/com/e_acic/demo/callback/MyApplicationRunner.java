package com.e_acic.demo.callback;
/*
webpro
com.e_acic.demo.callback
@auth hdh
2024/7/6  17:02
description:
*/

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationRunner implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("啓動初期0");
    }
}
