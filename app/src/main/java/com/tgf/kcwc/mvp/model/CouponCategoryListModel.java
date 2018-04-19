package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/1/24 0024
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponCategoryListModel {
    @JsonProperty("list")
    public ArrayList<Coupon> couponArrayList;
    @JsonProperty("pagination")
    public Pagination pagination;
}
