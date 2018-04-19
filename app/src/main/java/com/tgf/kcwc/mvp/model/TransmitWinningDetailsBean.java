package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransmitWinningDetailsBean {
    public int    code;

    public String msg;

    public Data   data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        public Details          details;
        public Share share;

        @JsonProperty("forward_info")
        public ForwardInfo      forwardInfo;

        public Create           create;
        @JsonProperty("record_list")
        public List<RecordList> recordList;
        @JsonProperty("prize_list")
        public List<PrizeList>  prizeList;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Details {
        public int    id;

        public String name;

        public String cover;
        @JsonProperty("forward_type")
        public int    forwardType;
        @JsonProperty("forward_id")
        public int    forwardId;
        @JsonProperty("start_time")
        public String startTime;
        @JsonProperty("end_time")
        public String endTime;

        public int    type;

        public int    limits;
        @JsonProperty("limits_type")
        public int    limitsType;
        @JsonProperty("wheel_nums")
        public int    wheelNums;
        @JsonProperty("is_on")
        public int    isOn;
        @JsonProperty("total_price")
        public String    totalPrice;

        public int    second;

        public int    times;
        @JsonProperty("have_nums")
        public int    haveNums;
        @JsonProperty("is_receive")
        public int    isReceive;

        public String intro;

        public String rules;

        public String desc;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Share {

        public String title;

        public String desc;

        public String url;

        public String img;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForwardInfo {
        public int    type;

        public String name;

        public String    price;
        @JsonProperty("use_time_start")
        public String useTimeStart;
        @JsonProperty("use_time_end")
        public String useTimeEnd;

        public int    id;
        @JsonProperty("event_name")
        public String eventName;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Create {
        public String name;

        public String logo;

        public String address;

        public String tel;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecordList {
        public String name;

        public String score;
        @JsonProperty("prize_name_sub")
        public String prizeNameSub;

        public String avatar;

        public int    uid;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PrizeList {
        public int id;

        public String name;

        public int    type;
        @JsonProperty("prize_type")
        public int    prizeType;

        public int    nums;

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

        public String remarks;
    }

}
