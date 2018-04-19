package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/9/21
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayParamModel {
    @JsonProperty("code")
    public int    statusCode;
    @JsonProperty("msg")
    public String statusMessage;
    @JsonProperty("data")
    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data{
        @JsonProperty("partnerid")
        public String partnerId;
        @JsonProperty("prepayid")
        public String prepayId;
        @JsonProperty("appid")
        public String appId;
        @JsonProperty("timestamp")
        public String timeStamp;
        @JsonProperty("noncestr")
        public String nonceStr;
        @JsonProperty("package")
        public String wxPackage;
        @JsonProperty("sign")
        public String paySign;
    }
}
