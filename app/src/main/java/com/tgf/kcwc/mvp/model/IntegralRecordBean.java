package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntegralRecordBean {
    public int code;

    public String msg;

    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public int num;

        public int id;
        @JsonProperty("product_type")
        public int productType;
        @JsonProperty("receive_status")
        public int receiveStatus;
        @JsonProperty("use_points")
        public int usePoints;
        @JsonProperty("product_id")
        public int productId;

        public String info;
        @JsonProperty("receive_time")
        public String receiveTime;
        @JsonProperty("receive_type")
        public int receiveType;

        public String address;

        public String tel;

        public String nickname;

        public String cover;

        public String name;

        public String code;

    }

}
