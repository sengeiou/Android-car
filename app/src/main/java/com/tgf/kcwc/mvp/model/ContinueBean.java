package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContinueBean implements Serializable{
    public int code;

    public String msg;

    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data implements Serializable{
        @JsonProperty("activity_line")
        public ActivityLine activityLine;
        @JsonProperty("activity_line_node")
        public List<ActivityLineNode> activityLineNode;
        @JsonProperty("ride_line_node")
        public List<ActivityLineNode> rideLineNode;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ActivityLine implements Serializable{

        public int id;
        @JsonProperty("thread_id")
        public int threadId;

        @JsonProperty("end_adds")
        public String endAds;

        @JsonProperty("end_lng")
        public String endLng;

        @JsonProperty("end_lat")
        public String endLat;

        public double mileage;

        public String cover;


    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ActivityLineNode implements Serializable{

        public String type;

        public int id;

        public int orders;

        public String lat;

        public String lng;

        public String address;
        @JsonProperty("is_light")
        public int isLight;

        public String title;
        @JsonProperty("user_id")
        public int userId;
        @JsonProperty("ride_id")
        public int rideId;
        @JsonProperty("city_name")
        public String cityName;

        public double mileage;
    }

}
