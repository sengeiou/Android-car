package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrivingRoadBookBean implements Serializable {
    public int    code;

    public String msg;

    public Data   data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("plan_line")
        public PlanLine        planLine;
        @JsonProperty("already_ride")
        public AlreadyRide     alreadyRide;
        @JsonProperty("ride_check")
        public List<RideCheck> rideCheck;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlanLine {
        public int    id;
        public String cover;
        public String mileage;
        @JsonProperty("plan_time")
        public String planTime;
        public String number;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AlreadyRide {
        public String mileage;
        public int    orders;
        public int    percent;
        public String time;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RideCheck implements Serializable {
        public int    id;
        @JsonProperty("city_name")
        public String cityName;
        public String address;
        public int    num;
        @JsonProperty("is_light")
        public int    isLight;
        @JsonProperty("light_time")
        public String lightTime;
        public double mileage;
        @JsonProperty("previous_time")
        public String previousTime;
    }

}
