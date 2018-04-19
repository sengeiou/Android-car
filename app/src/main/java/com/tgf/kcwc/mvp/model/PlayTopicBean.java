package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2017/5/12.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayTopicBean {
    public int id;

    public String title;

    @JsonProperty("fans_num")
    public int fansNum;

    public String cover;

}
