package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/6/1.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompileDrivingBean {
    public int code;

    public String msg;

    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public int id;

        public String cover;

        public String title;
        @JsonProperty("scene_type")
        public int sceneType;

        public String start;
        @JsonProperty("start_city")
        public String startCity;
        @JsonProperty("start_longitude")
        public double startLongitude;
        @JsonProperty("start_latitude")
        public double startLatitude;

        public String destination;
        @JsonProperty("dest_city")
        public String destCity;
        @JsonProperty("dest_longitude")
        public double destLongitude;
        @JsonProperty("dest_latitude")
        public double destLatitude;
        @JsonProperty("roadbook_id")
        public int roadbookId;
        @JsonProperty("begin_time")
        public String beginTime;
        @JsonProperty("end_time")
        public String endTime;

        public String budget;
        @JsonProperty("limit_min")
        public int limitMin;
        @JsonProperty("limit_max")
        public int limitMax;
        @JsonProperty("deadline_time")
        public String deadlineTime;
        @JsonProperty("only_owner")
        public int onlyOwner;
        @JsonProperty("need_review")
        public int needReview;
        @JsonProperty("other_condition")
        public String otherCondition;

        public String intro;
        @JsonProperty("roadbook_title")
        public String roadbookTitle;
        @JsonProperty("topic_list")
        public List<TopicList> topicList;

        public int number;

        public String longitude;
        public String latitude;
        public String adcode;
        @JsonProperty("local_address")
        public String localAddress;
        @JsonProperty("start_adcode")
        public String startAdcode;
        @JsonProperty("dest_adcode")
        public String destAdcode;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TopicList {
        public int id;

        public String title;

    }

}
