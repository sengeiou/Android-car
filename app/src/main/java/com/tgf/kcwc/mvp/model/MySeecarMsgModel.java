package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/4/19 0019
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MySeecarMsgModel {
    @JsonProperty("list")
    public ArrayList<OrderItem> orderList;
    @JsonProperty("pagination")
    public Pagination pagination;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderItem{
        @JsonProperty("order_sn")
        public String orderSn;
        public String cover;
        public int id;
        public String tel;
        public String contact;
        public String note;
        @JsonProperty("create_time")
        public String createTime;
        @JsonProperty("expire_time")
        public String expireTime;
        public int status;
        public String description;
        @JsonProperty("out_color_name")
        public String outColorName;
        @JsonProperty("in_color_name")
        public String inColorName;
        @JsonProperty("factory_name")
        public String factoryName;
        @JsonProperty("series_name")
        public String seriesName;
        @JsonProperty("car_name")
        public String carName;
        @JsonProperty("city_name")
        public String cityName;
    }
}
