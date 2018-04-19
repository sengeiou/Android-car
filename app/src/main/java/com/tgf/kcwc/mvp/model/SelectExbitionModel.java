package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @anthor noti
 * @time 2017/8/24
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SelectExbitionModel {
    public int id;

    public String cover;

    public String name;
    @JsonProperty("start_time")
    public String startTime;
    @JsonProperty("end_time")
    public String endTime;

    public String address;
}
