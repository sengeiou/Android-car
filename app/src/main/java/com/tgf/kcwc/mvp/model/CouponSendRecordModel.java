package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/2/15 0015
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponSendRecordModel {
    @JsonProperty("get_time_limit")
    public String get_time_limit;
    @JsonProperty("time")
    public String time;
    @JsonProperty("list")
    public List<SendRecord> list;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SendRecord{
        @JsonProperty("id")
        public int id;
        @JsonProperty("num")
        public int num;
        @JsonProperty("user")
        public User user;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User{
        @JsonProperty("id")
        public int id;
        @JsonProperty("nickname")
        public String nickname;
        @JsonProperty("tel")
        public String tel;
    }
}
