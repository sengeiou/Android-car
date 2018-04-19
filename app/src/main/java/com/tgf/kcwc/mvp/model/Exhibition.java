package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2016/12/22 0022
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Exhibition {
    @JsonProperty("id")
    public int          id;
    @JsonProperty("name")
    public String       name;
    @JsonProperty("address")
    public String       address;
    @JsonProperty("time")
    public String       time;
    @JsonProperty("start_time")
    public String       startTime;
    @JsonProperty("end_time")
    public String       endTime;
    @JsonProperty("server_time")
    public String       current_time;
    @JsonProperty("cover")
    public String       coverImageurl;
    @JsonProperty("img")
    public String       coverImg;
    @JsonProperty("is_ticket_g")
    public int          isTicketg;
    @JsonProperty("is_ticket_l")
    public int          isTicketl;
    @JsonProperty("is_close")
    public int          isClose;
    @JsonProperty("latitude")
    public String          latitude;
    @JsonProperty("longitude")
    public String          longitude;
    @JsonProperty("nextid")
    public int nextid;
    @JsonProperty("pid")
    public int pid;
//    @JsonProperty("plink")
//    public ArrayList<String> pImageUrl;

    public String       desc;
    public int          isShopping;
    //展位图
    public List<String> palaceImage   = new ArrayList<String>();
    //展会图片
    public List<String> lastShowImage = new ArrayList<String>();
}
