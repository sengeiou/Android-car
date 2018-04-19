package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/6/1.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompilePleaseBean {
    public int code;

    public String msg;

    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        public int id;

        public String cover;

        public String title;

        public String type;

        public String rendezvous;
        @JsonProperty("rend_city")
        public String rendCity;
        @JsonProperty("rend_longitude")
        public double rendLongitude;
        @JsonProperty("rend_latitude")
        public double rendLatitude;
        @JsonProperty("begin_time")
        public String beginTime;

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
        @JsonProperty("limit_min")
        public int limitMin;
        @JsonProperty("limit_max")
        public int limitMax;
        @JsonProperty("only_owner")
        public int onlyOwner;
        @JsonProperty("need_review")
        public int needReview;
        @JsonProperty("deadline_time")
        public String deadlineTime;
        @JsonProperty("other_condition")
        public String otherCondition;

        public String intro;
        @JsonProperty("end_time")
        public String endTime;
        @JsonProperty("roadbook_title")
        public String roadbookTitle;
        @JsonProperty("car_list")
        public List<CarList> carList;
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
        @JsonProperty("rend_adcode")
        public String rendAdcode;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TopicList {
        public int id;

        public String title;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CarList {
        public int id;

        public String cover;
        @JsonProperty("car_name")
        public String carName;
        @JsonProperty("factory_name")
        public String factoryName;
        @JsonProperty("series_name")
        public String seriesName;
    }

}
