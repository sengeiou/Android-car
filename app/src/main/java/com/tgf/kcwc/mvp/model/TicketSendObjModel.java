package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/7 0007
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketSendObjModel {
    @JsonProperty("list")
    public ArrayList<User> ticketUser;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        @JsonProperty("tfh_id")
        public int    tfhId;
        @JsonProperty("mobile")
        public String mobile;
        @JsonProperty("user_type")
        public int    userType;
        @JsonProperty("user_name")
        public String userName;
        @JsonProperty("user_avatar")
        public String userAvatar;
        @JsonProperty("nums")
        public Nums nums;
    }

    public static class Nums {
        @JsonProperty("receive")
        public int receive;
        @JsonProperty("expire")
        public int expire;
        @JsonProperty("all")
        public int all;
        @JsonProperty("lose")
        public int lose;
        @JsonProperty("use")
        public int use;
    }
}
