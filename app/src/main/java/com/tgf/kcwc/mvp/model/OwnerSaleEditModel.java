package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/11/7
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OwnerSaleEditModel {
    @JsonProperty("code")
    public int    statusCode;

    @JsonProperty("msg")
    public String statusMessage;
    @JsonProperty("data")
    public ArrayList<Data> data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data{

    }
}
