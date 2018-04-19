package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/2/15 0015
 * E-mail:hekescott@qq.com
 * 分发对象
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponSendObjModel {
    @JsonProperty("id")
    public int id;
    @JsonProperty("num")
    public int num;
    @JsonProperty("get_num")
    public int getNum;
    @JsonProperty("check_num")
    public int checkNum;
    @JsonProperty("lose_num")
    public int lose_num;
    @JsonProperty("expiration_num")
    public int expirationNum;
    @JsonProperty("user")
    public CouponUser user;

    public static class CouponUser {
        @JsonProperty("id")
        public int id;
        @JsonProperty("nickname")
        public String nickname;
        @JsonProperty("tel")
        public String tel;
        @JsonProperty("avatar")
        public String avatar;
    }
}
