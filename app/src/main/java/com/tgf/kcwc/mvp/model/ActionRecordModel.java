package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * author:noti
 * date:2017/8/14
 * describe:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActionRecordModel {

    @JsonIgnoreProperties("user")
    public UserItem userItem;
    @JsonProperty("pagination")
    public Pagination pagination;
    @JsonProperty("list")
    public ArrayList<ActionRecordModel.ListItem> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserItem {
        public int id;
        public String tel;
        @JsonProperty("user_id")
        public int userId;
        public String avatar;
        public String name;
        public int sex;
        public String birthday;
        @JsonProperty("is_model")
        public int isModel;
        @JsonProperty("is_master")
        public int isMaster;
        @JsonProperty("is_doyen")
        public int isDoyen;
        public int age;
        public String logo;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListItem {
        public String type;
        public String keywords;
        @JsonProperty("create_time")
        public String createTime;
    }
}
