package com.tgf.kcwc.mvp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2017/4/24.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PleaseFoundListBean {
    public int    code;

    public String msg;

    public Data   data;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("is_auth")
        public int            isAuth;

        public Pagination     pagination;

        public List<DataList> list;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {
        public int    id;

        public String title;

        public String cover;
        @JsonProperty("group_id")
        public int    groupId;

        @JsonProperty("type")
        public String type;

        @JsonProperty("org_id")
        public int    orgId;

        @JsonProperty("like_count")
        public int    likeCount;

        @JsonProperty("reply_count")
        public int    replyCount;

        @JsonProperty("view_count")
        public int    viewCount;

        @JsonProperty("begin_time")
        public String beginTime;

        @JsonProperty("end_time")
        public String endTime;

        @JsonProperty("deadline_time")
        public String deadlineTime;

        public String start;

        @JsonProperty("start_city")
        public String startCity;

        public String destination;

        @JsonProperty("dest_city")
        public String destCity;

        public int    num;
        @JsonProperty("pass_num")
        public int    passNum;

        @JsonProperty("activity_status")
        public int    activityStatus;

        @JsonProperty("need_review")
        public int    needReview;

        @JsonProperty("rendezvous")
        public String rendezvous;

        @JsonProperty("rend_city")
        public String rendCity;

        @JsonProperty("roadbook_id")
        public int roadbookId;
    }

}
