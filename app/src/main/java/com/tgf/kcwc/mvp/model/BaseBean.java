package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseBean {
    @JsonProperty("code")
    public int    code;
    @JsonProperty("msg")
    public String msg;

    @JsonProperty("data")
    public Data   data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
    }
}
