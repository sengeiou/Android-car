package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntegralGoodListBean {
    public int code;

    public String msg;

    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public int count;

        public List<DataList> list;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {

        public int id;

        public String name;

        public String cover;
        @JsonProperty("need_points")
        public int needPoints;
        @JsonProperty("market_value")
        public String marketValue;
        @JsonProperty("exchange_type")
        public int exchangeType;

        public String address;
        @JsonProperty("plam_or_shopping")
        public int plamOrShopping;

    }

}
