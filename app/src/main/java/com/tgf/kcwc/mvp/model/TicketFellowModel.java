package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/9 0009
 * E-mail:hekescott@qq.com
 * 门票跟踪
 */

public class TicketFellowModel {
    @JsonProperty("nums_expire")
    public int                   nums_expire;
    @JsonProperty("nums_lose")
    public int                   nums_lose;
    @JsonProperty("list")
    public ArrayList<FellowItem> fellowContents;
    @JsonProperty("user")
    public FellowUser            user;

    public static class FellowItem {
        @JsonProperty("time")
        public String date;
        @JsonProperty("type")
        public String type;
        @JsonProperty("msg_type")
        public int msgType;
        @JsonProperty("content")
        public String content;
        @JsonProperty("title")
        public String title;
        @JsonProperty("ticket")
        public String ticket;
        @JsonProperty("nums")
        public int    nums;
    }

    public static class FellowUser {
        public String nickname;
        public String mobile;
        public String avatar;

    }
}
