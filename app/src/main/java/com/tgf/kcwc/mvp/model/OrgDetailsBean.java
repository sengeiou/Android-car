package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2017/5/4.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgDetailsBean {
    @JsonProperty("code")
    public int code;
    @JsonProperty("msg")
    public String msg;

    @JsonProperty("data")
    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("current_points")
        public int currentPoints;
        @JsonProperty("current_exps")
        public int currentExps;
        @JsonProperty("used_points")
        public int usedPoints;
        @JsonProperty("get_points")
        public int getPoints;

        public String name;
        @JsonProperty("full_name")
        public String fullName;

        public int exp;

        public int points;

        public int level;
        @JsonProperty("admin_tel")
        public String adminTel;
        @JsonProperty("user_type")
        public int userType;
        @JsonProperty("next_level")
        public int nextLevel;

        public String avatar;
        @JsonProperty("user_money")
        public String userMoney;

    }
}
