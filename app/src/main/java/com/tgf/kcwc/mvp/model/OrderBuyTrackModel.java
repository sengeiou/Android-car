package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/3/10 0010
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderBuyTrackModel {
    @JsonProperty("car_info")
    public CarInfo carInfo;
    @JsonProperty("user_info")
    public UserInfo userInfo;
    @JsonProperty("off_note")
    public String offNote;
    @JsonProperty("order_info")
    public OrderInfo orderInfo;

    public static class CarInfo {
        public String car_name;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderInfo {
        public String contact;
        public String description;
        public int status;
        public String lng;
        public String lat;
        public String out_color_name;
        public String in_color_name;
        public String out_color_value;
        @JsonProperty("in_color_value")
        public String inColorValue;
        @JsonProperty("factory_name")
        public String factoryName;
        @JsonProperty("series_name")
        public String seriesName;
        public String car_name;
        public String tel;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserInfo {
        public String avatar;
        public String username;
        public int sex;
        @JsonProperty("is_doyen")
        public int isDa;
        @JsonProperty("is_model")
        public int isModel;
        @JsonProperty("is_master")
        public int isMaster;
        @JsonProperty("master_brand")
        public String masterBrand;
        @JsonProperty("nickname")
        public String nickname;

    }
}
