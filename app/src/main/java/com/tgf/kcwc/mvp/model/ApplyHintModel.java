package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @anthor noti
 * @time 2017/11/6
 * @describle
 */
public class ApplyHintModel {
    public String content;
    public String title;
    @JsonProperty("event_name")
    public String eventName;

}
