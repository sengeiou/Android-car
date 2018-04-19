package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @anthor noti
 * @time 2017/11/6
 * @describle
 */
public class GetRemainModel {
    @JsonProperty("code")
    public int    statusCode;
    public int    data;

    @JsonProperty("msg")
    public String statusMessage;
}
