package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntegralDiamondListBean {
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
        @JsonProperty("point_type")
        public int pointType;

        public int points;

        public int price;

        public int uid;
        @JsonProperty("sale_status")
        public int saleStatus;

        public String nikename;

    }

}
