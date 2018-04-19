package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/1/3 0003
 * E-mail:hekescott@qq.com
 * 展会活动
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExhibitEvent {
    /**展会id*/
    @JsonProperty("event_id")
    public int                  eventId;
    /**活动id*/
    @JsonProperty("id")
    public int                  id;
    public int                  content;
    @JsonProperty("img_count")
    public int                  imgCount;
    @JsonProperty("title")
    public String               title;
    @JsonProperty("start_time")
    public String               startTime;
    @JsonProperty("end_time")
    public String               endTime;
    @JsonProperty("cover")
    public String               cover;
    @JsonProperty("hosts")
    public String               hosts;
    @JsonProperty("is_end")
    public int               isEnd;
    @JsonProperty("organizers")
    public String               organizers;
    @JsonProperty("description")
    public String               description;
    //协办方
    @JsonProperty("sponsors")
    public String               sponsors;
    @JsonProperty("create_time")
    public String               createTime;
    @JsonProperty("hall_name")
    public String               hallName;
    @JsonProperty("booth_name")
    public String               boothName;
    @JsonProperty("html")
    public String               html;
    @JsonProperty("guest_list")
    public ArrayList<Guest>     guestlist;
    @JsonProperty("thread_list")
    public ArrayList<QzoneItem> qzoneList;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Guest {
        @JsonProperty("id")
        public int    id;
        @JsonProperty("name")
        public String name;
        @JsonProperty("avatar")
        public String avatar;
        @JsonProperty("title")
        public String title;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QzoneItem {
        @JsonProperty("cover")
        public String cover;
        @JsonProperty("create_time")
        public String createtime;
        @JsonProperty("title")
        public String title;
    }
}
