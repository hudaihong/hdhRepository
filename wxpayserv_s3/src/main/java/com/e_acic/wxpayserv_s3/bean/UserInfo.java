package com.e_acic.wxpayserv_s3.bean;

/*
webpro
com.e_acic.wxpayserv_s3.bean
@auth  hdh
description:
2024-07-05 9:43
*/

import lombok.Data;

import java.io.Serializable;

@Data

public class UserInfo implements Serializable {


    public UserInfo() {

    }

    private String userName;
    private String userPwd;
}
