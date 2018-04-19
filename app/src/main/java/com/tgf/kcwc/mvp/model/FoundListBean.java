package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoundListBean {
    public int    code;

    public String msg;

    public Data   data;

    public static class Data {
        public Pagination     pagination;

        public List<DataList> list;
    }

    public static class DataList {
        public int id;

        public String title;

        public String cover;
        @JsonProperty("group_id")
        public int groupId;
        @JsonProperty("view_count")
        public int viewCount;
        @JsonProperty("digg_count")
        public int diggCount;
        @JsonProperty("reply_count")
        public int replyCount;
        @JsonProperty("like_count")
        public int likeCount;
        @JsonProperty("scene_type")
        public int sceneType;
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

        public int num;
        @JsonProperty("pass_num")
        public int passNum;
        @JsonProperty("activity_status")
        public int activityStatus;

        @JsonProperty("need_review")
        public int    needReview;

        @JsonProperty("roadbook_id")
        public int roadbookId;

    }

}
