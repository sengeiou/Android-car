package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2017/4/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntegralPurchaseBean {
    public int code;

    public String msg;

    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("point_type")
        public int pointType;

        public int id;

        public int points;

        public int price;

    }

}
