package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/4/11 0011
 * E-mail:hekescott@qq.com
 */

public class CouponPayModel {
    @JsonProperty("is_pay")
    public int isPay;
    @JsonProperty("total_price")
    public String totalPrice;

}
