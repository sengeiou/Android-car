package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntegralConversionGoodDetailsBean {
    @JsonProperty("code")
    public int code;
    @JsonProperty("msg")
    public String msg;

    @JsonProperty("data")
    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public Details details;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Details {

        public int id;

        public String name;
        @JsonProperty("need_points")
        public int needPoints;

        public String cover;
        @JsonProperty("market_value")
        public String marketValue;

        public int stock;

        public String describe;
        @JsonProperty("on_time")
        public String onTime;
        @JsonProperty("down_time")
        public String downTime;
        @JsonProperty("plam_or_shopping")
        public int plamOrShopping;
        @JsonProperty("exchange_type")
        public int exchangeType;

        public String tel;
        public String address;
        public String uername;
    }
}
