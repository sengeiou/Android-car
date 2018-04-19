package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/8/8 0008
 * E-mail:hekescott@qq.com
 */

public class MyCouponModel extends BasePageModel{

    public ArrayList<MyCouponOrder>  list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MyCouponOrder{
        public int id;
        @JsonProperty("order_sn")
        public String orderSn;
        @JsonProperty("is_expiration")
        public int isExpiration;
        public MyCoupon coupon;
        public ArrayList<CouponCode> codes;
        public ArrayList<MyCouponDealers> dealers;
        @JsonProperty("issue_org")
        public MyCouponDealers issueOrg;
        @JsonProperty("online")
        public int onlienNum;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MyCoupon{
        public int id;
        public String title;
        @JsonProperty("end_time")
        public String endTime;
        public String price;
        @JsonProperty("send_type")
        public int sendType;
        public String denomination;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MyCouponDealers{

        public int id;
        public String name;
        @JsonProperty("full_name")
        public String fullName;
        public String address;
        public String tel;
        public String longitude;
        public String latitude;
        public String logo;
        public String distance;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static  class CouponCode{
        public int id;
        public String code;
        public int status;
        @JsonProperty("status_time")
        public String statusTime;
    }

}
