package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/14
 * @describle
 */
public class RecordActivityModel {
    @JsonProperty("list")
    public ArrayList<RecordActivityModel.ListItem> list;

    @JsonProperty("user")
    public RecordActivityModel.UserItem userItem;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListItem{
        @JsonProperty("activity_status")
        public String activityStatus;
        @JsonProperty("begin_time")
        public String beginTime;
        @JsonProperty("cancel_time")
        public String cancelTime;
        @JsonProperty("create_by")
        public String createBy;
        @JsonProperty("create_time")
        public String createTime;
        public String destination;
        @JsonProperty("end_time")
        public String endTime;
        public int id;
        @JsonProperty("is_cancel")
        public int isCancel;
        @JsonProperty("is_mine")
        public int isMine;
        public String model;
        public int num;
        public String rendezvous;
        public String start;
        public String title;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserItem{
        public String avatar;
        public String birthday;
        public int id;
        @JsonProperty("is_doyen")
        public int isDoyen;
        @JsonProperty("is_master")
        public int isMaster;
        @JsonProperty("is_model")
        public int isModel;
        public String logo;
        public String nickname;
        public int sex;
        public String tel;
        @JsonProperty("user_id")
        public int userId;
        public int age;
    }
}
