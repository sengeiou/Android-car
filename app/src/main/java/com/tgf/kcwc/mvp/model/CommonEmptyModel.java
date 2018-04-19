package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/11/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonEmptyModel {

    /**
     * code : 1
     * msg : 延迟失败
     * data : []
     */
    @JsonProperty("code")
    public int statusCode;
    @JsonProperty("msg")
    public String statusMessage;
    @JsonProperty("data")
    public List<?> data;
}
