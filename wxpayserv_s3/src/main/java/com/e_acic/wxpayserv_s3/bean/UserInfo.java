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
import java.sql.Timestamp;

@Data
public class UserInfo implements Serializable {
    private Integer wid;
    private String token;
    private String openid;
    private Boolean deal_flag;
    private Timestamp in_time;
}
