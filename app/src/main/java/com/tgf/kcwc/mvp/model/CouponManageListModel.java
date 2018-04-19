package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/14 0014
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponManageListModel {
    @JsonProperty("pagination")
    public Pagination pagination;
    @JsonProperty("list")
    public ArrayList<CouponManageItem> list;

    public static class CouponManageItem {
        @JsonProperty("id")
        public int id;
        @JsonProperty("count")
        public int count;
        @JsonProperty("inventory")
        public int inventory;
        @JsonProperty("is_expiration")
        public int isExpiration;
        @JsonProperty("coupon")
        public Coupon coupon;
    }
}
