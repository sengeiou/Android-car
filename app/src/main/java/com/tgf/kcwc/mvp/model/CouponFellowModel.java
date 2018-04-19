package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/16 0016
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponFellowModel {
    @JsonProperty("list")
    public ArrayList<CouponFellowItem> couponFellowItems;
    @JsonProperty("user")
    public CouponReciever couponReciever;
//
//    public static class DataBean {
//        @JsonProperty("check_type")
//        public String checkType;
//        @JsonProperty("checkFrom")
//        public String checkFrom;
//        @JsonProperty("code")
//        public List<String> code;
//    }
@JsonIgnoreProperties(ignoreUnknown = true)
    public static class CouponFellowItem {
        @JsonProperty("msg_type")
        public String msgType;
        @JsonProperty("type")
        public String type;
        @JsonProperty("h1")
        public String h1;
        @JsonProperty("h2")
        public String h2;
        @JsonProperty("create_time")
        public String createTime;
        @JsonProperty("title")
        public String title;
        @JsonProperty("expiration_time")
        public String expirationTime;
        @JsonProperty("data")
        public ArrayList<String> data;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CouponReciever{
        @JsonProperty("id")
        public int id;
        @JsonProperty("nickname")
        public String nickname;
        @JsonProperty("tel")
        public String mobile;
        @JsonProperty("avatar")
        public String avatar;
    }
}
