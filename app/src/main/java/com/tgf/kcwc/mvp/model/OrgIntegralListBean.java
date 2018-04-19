package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgIntegralListBean {
    public int code;

    public String msg;

    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public int count;
        public Pagination pagination;

        public List<DataList> list;

        public Info info;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {

        public int id;

        public String name;

        public int uid;

        public String exps;

        public String points;

        public String remarks;
        @JsonProperty("create_time")
        public String createTime;

        public int type;
        @JsonProperty("org_id")
        public int orgId;

        public String tel;

        public String nickname;

        public String crease;


    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Info {
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
        @JsonProperty("next_level")
        public int nextLevel;

        public String avatar;
    }

}
