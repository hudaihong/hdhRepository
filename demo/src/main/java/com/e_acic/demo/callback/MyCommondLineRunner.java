package com.e_acic.demo.callback;
/*
webpro
com.e_acic.demo.callback
@auth hdh
2024/7/6  16:59
description:
*/

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyCommondLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("啓動初期1");
    }
}
