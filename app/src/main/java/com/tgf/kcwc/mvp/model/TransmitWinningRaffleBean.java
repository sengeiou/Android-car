package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransmitWinningRaffleBean {
    @JsonProperty("code")
    public int code;
    @JsonProperty("msg")
    public String msg;

    @JsonProperty("data")
    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        public List<DataList> list;

        public Nums  nums;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Nums {

        public int limits;
        @JsonProperty("limits_type")
        public int limitsType;
        @JsonProperty("have_nums")
        public int haveNums;
        @JsonProperty("can_nums")
        public int canNums;

    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForwardInfo {

        public int id;
        public String name;

        public String cover;
        @JsonProperty("event_id ")
        public int eventId;


    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {
        public String name;

        public int id;

        public int type;

        @JsonProperty("prize_type")
        public int prizeType;

        public int nums;

        public String img;

        @JsonProperty("sub_name")
        public String subName;

        public String color;

        public String price;

        @JsonProperty("use_time_start")
        public String useTimeStart;

        @JsonProperty("use_time_end")
        public String useTimeEnd;

        @JsonProperty("event_name")
        public String eventName;
    }
}
