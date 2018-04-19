package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/2/6 0006
 * E-mail:hekescott@qq.com
 * 展会
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    @JsonProperty("can_get_nums")
    public int limitNums;
    @JsonProperty("id")
    public int id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("cover")
    public String cover;
    @JsonProperty("ticket_name")
    public String ticketName;
    @JsonProperty("nums")
    public int nums;

}
