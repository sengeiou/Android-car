package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntegralRecordListBean {
    public int code;

    public String msg;

    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public Pagination pagination;

        public List<DataList> list;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {

        public int id;
        @JsonProperty("create_time")
        public String createTime;
        @JsonProperty("receive_time")
        public String receiveTime;

        public int num;
        @JsonProperty("use_points")
        public int usePoints;
        @JsonProperty("receive_type")
        public int receiveType;
        @JsonProperty("product_type")
        public int productType;
        @JsonProperty("product_name")
        public String productName;
        @JsonProperty("need_points")
        public int needPoints;

    }

}
