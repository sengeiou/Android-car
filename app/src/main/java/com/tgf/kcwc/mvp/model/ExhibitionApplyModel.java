package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/9/15
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExhibitionApplyModel {
    @JsonProperty("code")
    public int    statusCode;
    public int    emptyIdentifier;
    @JsonProperty("msg")
    public String statusMessage;
    @JsonProperty("data")
    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data{
        @JsonProperty("apply_id")
        public int applyId;
        @JsonProperty("order_sn")
        public String orderSn;
    }
}
