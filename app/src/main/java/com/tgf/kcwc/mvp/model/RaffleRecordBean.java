package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RaffleRecordBean {
    @JsonProperty("code")
    public int code;
    @JsonProperty("msg")
    public String msg;

    @JsonProperty("data")
    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        public List<DataList> list;

        public int count;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {
        public int id;

        public int fid;

        @JsonProperty("create_time")
        public String createTime;

        @JsonProperty("is_prize")
        public int isPrize;

        @JsonProperty("fp_id")
        public int fpId;
        @JsonProperty("forward_prize_info")
        public ForwardPrizeInfo forwardPrizeInfo;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForwardPrizeInfo {
        public int id;
        public int type;
        @JsonProperty("prize_type")
        public int prizeType;

        public String remarks;
    }
}
