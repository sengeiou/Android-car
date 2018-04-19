package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2017/9/27.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LikeBean {

    @JsonProperty("is_praise")
    public int isPraise;
    @JsonProperty("digg_count")
    public int diggCount;
}
