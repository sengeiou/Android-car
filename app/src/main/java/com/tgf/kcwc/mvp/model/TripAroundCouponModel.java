package com.tgf.kcwc.mvp.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/5/16 0016
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripAroundCouponModel {
    @JsonProperty("coupon_list")
    public CouponList modelList;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CouponList{
        @JsonProperty("list")
        public ArrayList<TripBookDetailModel.Coupon> couponList;

        public Pagination pagination;
    }
}
