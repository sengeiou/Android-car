package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeCrunchiesBean {
    public int code;

    public String msg;

    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public List<DataList> list;

        @JsonProperty("have_self")
        public int haveSelf;

        public String time;

        public User user;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {
        public int id;

        public String nickname;

        public String value;

        public String avatar;

        public String level;

        public String index;

        public int self;
        @JsonProperty("apply_nums")
        public int applyNums;
        @JsonProperty("active_nums")
        public int activeNums;
        @JsonProperty("service_score")
        public double serviceScore;

        public int sex;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        public int id;

        public String avatar;

        public String nickname;

        public String value;

        public String index;

        public int level;
        @JsonProperty("apply_nums")
        public int applyNums;

        private int active_nums;

        private int service_score;

        public int sex;
    }

}
