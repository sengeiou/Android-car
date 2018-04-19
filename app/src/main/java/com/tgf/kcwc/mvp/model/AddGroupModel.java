package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/8 17:20
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddGroupModel {
    @JsonProperty("code")
    public int statusCode;
    @JsonProperty("msg")
    public String statusMessage;

    @JsonProperty("data")
    public ArrayList<ListItem> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListItem {

    }
}
