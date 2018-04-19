package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/9/29
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripBookRecordModel {
    @JsonProperty("code")
    public int    statusCode;
    @JsonProperty("msg")
    public String statusMessage;

    @JsonProperty("data")
    public ArrayList<Data> data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public int id;
        public String poi;
        public String lat;
        public String lng;
        @JsonProperty("add_time")
        public String addTime;
        @JsonProperty("thread_list")
        public ArrayList<ThreadList> threadList;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ThreadList {
            public int id;
            public String title;
            public String model;

            @JsonProperty("cover")
            public ArrayList<String> cover;

            public String longitude;
            public String latitude;
            @JsonProperty("local_address")
            public String localAddress;
            @JsonProperty("digg_count")
            public int diggCount;
            @JsonProperty("view_count")
            public int viewCount;
            @JsonProperty("create_time")
            public String createTime;
            @JsonProperty("s_address")
            public String sAddress;
            public String content;
            public String video;
            @JsonProperty("view_type")
            public int viewType;
            public String day;
            public String month;
            @JsonProperty("reply_count")
            public int replyCount;
        }
    }
}
