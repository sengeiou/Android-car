package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/10/9
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyExhibitionInfoModel {
    @JsonProperty("road_haul")
    public String roadHaul;
    public String statusName;
    @JsonProperty("event_address")
    public String eventAddress;
    @JsonProperty("car_series_name")
    public String carSeriesName;
    @JsonProperty("last_in_park_time")
    public String lastInParkTime;
    public String price;
    @JsonProperty("hall_name")
    public String hallName;
    @JsonProperty("event_name")
    public String eventName;
    @JsonProperty("in_park_time")
    public String inParkTime;
    @JsonProperty("car_image_in")
    public String carImageIn;
    @JsonProperty("car_name")
    public String carName;
    @JsonProperty("park_time_slot")
    public String parkTimeSlot;
    @JsonProperty("event_end_time")
    public String eventEndTime;
    @JsonProperty("apply_name")
    public String applyName;
    @JsonProperty("apply_phone")
    public String applyPhone;
    public int id;
    @JsonProperty("booth_cover")
    public String boothCover;
    @JsonProperty("apply_time")
    public String applyTime;
    @JsonProperty("car_image_out")
    public String carImageOut;
    @JsonProperty("end_time")
    public String endTime;
    @JsonProperty("event_start_time")
    public String eventStartTime;
    @JsonProperty("park_id")
    public int parkId;
    @JsonProperty("brand_name")
    public String brandName;
    @JsonProperty("buy_year")
    public int buyYear;
    public int status;
    @JsonProperty("park_time")
    public String parkTime;
    @JsonProperty("plate_number")
    public String plateNumber;
    @JsonProperty("out_place_time")
    public String outPlaceTime;
    @JsonProperty("in_place_time")
    public String inPlaceTime;
    @JsonProperty("time_id")
    public int timeId;
    @JsonProperty("event_id")
    public int eventId;
    @JsonProperty("hall_id")
    public int hallId;
    @JsonProperty("price_string")
    public int priceString;
    @JsonProperty("brand_logo")
    public String brandLogo;
    @JsonProperty("out_park_time")
    public String outParkTime;
    @JsonProperty("event_cover")
    public String eventCover;
    @JsonProperty("park_name")
    public String parkName;
    @JsonProperty("start_time")
    public String startTime;
    public String nickname;
    public String avatar;

    @JsonProperty("event_time")
    public EventTime eventTime;
    @JsonProperty("event_status")
    public EventStatus eventStatus;
    @JsonProperty("helpList")
    public ArrayList<HelpList> helpList;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EventTime {
        public String end;
        public String date;
        public String start;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EventStatus {
        @JsonProperty("start_status")
        public int startStatus;
        public int status;
        public String statusName;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HelpList {
        public String title;
        public String content;
    }
}
