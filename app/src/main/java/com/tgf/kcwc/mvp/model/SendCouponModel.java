package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Auther: Scott
 * Date: 2017/2/16 0016
 * E-mail:hekescott@qq.com
 */

public class SendCouponModel {
    @JsonProperty("nickname")
   public   String name;
    @JsonProperty("num")
   public   int num;
    @JsonProperty("tel")
   public   String tel;
    @JsonProperty("user_type")
   public   int userType;
}
