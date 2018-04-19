package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/8 14:21
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddCustomerModel {
    @JsonProperty("code")
    public int statusCode;
    @JsonProperty("msg")
    public String statusMessage;

    @JsonProperty("data")
    public ArrayList<FriendItem> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FriendItem {

    }
}
