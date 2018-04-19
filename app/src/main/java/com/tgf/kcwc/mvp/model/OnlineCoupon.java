package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/9/22 0022
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OnlineCoupon extends BasePageModel{
    @JsonProperty("list")
    public ArrayList<OnlineCouponItem> onlineCouponList;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OnlineCouponItem{
        public int id;
        public String title;
        public double price;
        public String cover;
        public String denomination;
        public String distance;
        public int total;
        @JsonProperty("is_finished")
        public int isfinished;
        @JsonProperty("issue_org")
        public String issueOrg;
        @JsonProperty("online")
        public ArrayList<OnlinePerson> OnlinePersonlist;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OnlinePerson{
        public int id;
        public String nickname;
        public String avatar;
        public String star;
    }
}
