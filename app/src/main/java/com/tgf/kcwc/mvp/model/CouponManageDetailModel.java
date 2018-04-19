package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/2/14 0014
 * E-mail:hekescott@qq.com
 */

public class CouponManageDetailModel {
    @JsonProperty("id")
    public int id;

    @JsonProperty("coupon")
    public Coupon coupon;
    @JsonProperty("statistics")
    public CouponStatistics statistics;

    public static class CouponStatistics{
        @JsonProperty("count")
        public  int count;
        @JsonProperty("send")
        public int send;
        @JsonProperty("inventory")
        public int inventory;
        @JsonProperty("get_num")
        public int getNum;
        @JsonProperty("get_user_num")
        public int userGetNum;
        @JsonProperty("check_num")
        public int checkNum;
        @JsonProperty("check_user_num")
        public int checkUserNum;
        @JsonProperty("goods_check_num")
        public int goodsCheckNum;
        @JsonProperty("goods_check_user_num")
        public int goodsCheckUserNum;

    }
}
