package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/1/6 0006
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponNearModel{
    /**数据总量*/
    private int count;
    /**每页条数*/
    private int limit;
    @JsonProperty("pagination")
    public  Pagination pagination;
    @JsonProperty("list")
    public  ArrayList<Component> componentList;

    public  static class Component{
        @JsonProperty("id")
        public int id;
        @JsonProperty("org")
        public Store store;
        @JsonProperty("coupon")
        public ArrayList<Coupon> couponList;
        public  boolean isExpand = false;

    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Store{
        @JsonProperty("id")
        public   int id;
        @JsonProperty("logo")
        public  String logo;
        @JsonProperty("full_name")
        public  String full_name;
        @JsonProperty("address")
        public  String address;
        @JsonProperty("star")
        public  int star;
        @JsonProperty("longitude")
        public String longitude;
        @JsonProperty("latitude")
        public String latitude;
        @JsonProperty("distance")
        public String distance;
        @JsonProperty("online")
        public int onlineNum;
    }


}
