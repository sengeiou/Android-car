package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransmitWinningHomeListBean {
    public int    code;

    public String msg;

    public Data   data;

    public static class Data {
        public Pagination     pagination;

        public int            count;

        public List<DataList> list;
    }

    public static class DataList {

        public int    id;

        public String name;

        public String cover;
        @JsonProperty("start_time")
        public String startTime;
        @JsonProperty("end_time")
        public String endTime;

        public int    type;

        public int    times;

        public int    second;
    }

}
