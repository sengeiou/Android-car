package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrvingListModel {
    @JsonProperty("code")
    public int  code;
    @JsonProperty("msg")
    public String  msg;
    @JsonProperty("data")
    public Data data;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("pagination")
        public Pagination     pagination;
        @JsonProperty("list")
        public List<DataList> list;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {
        @JsonProperty("destination")
        public String      destination;
        @JsonProperty("begin_time")
        public String      beginTime;
        @JsonProperty("start")
        public String      start;
        @JsonProperty("start_city")
        public String      startCity;
        @JsonProperty("dest_city")
        public String      destCity;
        @JsonProperty("deadline_time")
        public String      deadlineTime;
        @JsonProperty("limit_max")
        public int         limitMax;
        @JsonProperty("scene_type")
        public int         sceneType;
        @JsonProperty("id")
        public int         id;
        @JsonProperty("cover")
        public String      cover;
        @JsonProperty("title")
        public String      title;
        @JsonProperty("create_by")
        public int         createBy;
        @JsonProperty("create_time")
        public String      createTime;
        @JsonProperty("view_count")
        public int         viewCount;
        @JsonProperty("digg_count")
        public int         diggCount;
        @JsonProperty("reply_count")
        public int         replyCount;
        @JsonProperty("is_digest")
        public int         isDigest;
        @JsonProperty("create_user")
        public Create_user createUser;
        @JsonProperty("surplus")
        public int         surplus;
        @JsonProperty("activity_status")
        public String      activityStatus;
        public String      distance;

    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Create_user {
        @JsonProperty("id")
        public int    id;
        @JsonProperty("avatar")
        public String avatar;
        @JsonProperty("username")
        public String username;
        @JsonProperty("real_name")
        public String realName;
    }

}
