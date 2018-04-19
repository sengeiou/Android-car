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
public class HeadEvent {

    @JsonProperty("id")
    public int id;
    @JsonProperty("nums")
    public int nums;
    @JsonProperty("can_get_nums")
    public int limitNums;
    @JsonProperty("ht_user_nums")
    public int useNum;

    @JsonProperty("event_name")
    public String name;
    @JsonProperty("event_cover")
    public String cover;
    @JsonProperty("ticket_name")
    public String ticketName;

    public int getLeftNum(){
        return nums - useNum;
    }
}
