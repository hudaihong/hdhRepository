package com.e_acic.wxpayserv_s3.bean;
/*
webpro
com.e_acic.wxpayserv_s3.bean
@auth hdh
2024/7/7  19:54
description:
*/

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class OrderInfo implements Serializable {
    private String token;
    private String wx_order;
    private Timestamp wx_paytime;
    private Boolean wx_paysuccess;
    private String wx_payfailreason;
}
