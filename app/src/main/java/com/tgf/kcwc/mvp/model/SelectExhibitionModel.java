package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/9/13
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SelectExhibitionModel {
    @JsonProperty("code")
    public int    statusCode;
    @JsonProperty("msg")
    public String statusMessage;

    @JsonProperty("data")
    public ArrayList<List> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class List implements Serializable {
        public int id;
        public String name;
        public String address;
        @JsonProperty("start_time")
        public String startTime;
        @JsonProperty("end_time")
        public String endTime;
        public String cover;
        public int province;
        public int city;
        public int area;
        public String inputs;
        public int remaining;
        @JsonProperty("event_now_status")
        public int eventNowStatus;

        public boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }
}
