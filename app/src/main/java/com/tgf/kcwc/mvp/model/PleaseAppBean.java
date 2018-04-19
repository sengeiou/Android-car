package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PleaseAppBean {
    public int    code;

    public String msg;

    public Data   data;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public Pagination     pagination;

        public List<DataList> list;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {

        public int    id;
        @JsonProperty("thread_id")
        public int    threadId;

        public String title;

        public String cover;

        @JsonProperty("group_id")
        public int    groupId;

        @JsonProperty("type")
        public String type;

        @JsonProperty("deadline_time")
        public String deadlineTime;
        @JsonProperty("begin_time")
        public String beginTime;
        @JsonProperty("end_time")
        public String endTime;

        @JsonProperty("rendezvous")
        public String rendezvous;

        @JsonProperty("rend_city")
        public String rendCity;

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
        @JsonProperty("reply_count")
        public int    replyCount;

        @JsonProperty("roadbook_id")
        public int roadbookId;
    }
}
