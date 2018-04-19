package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @anthor noti
 * @time 2017/8/22
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HelpCenterDetailModel {
    public String content;
    public String title;
    public int id;
    @JsonProperty("is_hot")
    public int isHot;
}
