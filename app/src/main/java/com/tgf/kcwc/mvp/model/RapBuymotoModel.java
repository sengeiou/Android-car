package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/3/9 0009
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RapBuymotoModel {
    @JsonProperty("list")
    public ArrayList<RapbuyItem>  preBuymotolist;

    public Pagination pagination;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RapbuyItem {
        public int id;
        public int order_id;
        public String tel;
        public String contact;
        public String create_time;
        public int status;
        public String lng;
        public String avatar;
        public String lat;
        @JsonProperty("offer_count")
        public int offerCount;
        public String car_name;
        public String one_city_name;
        public String two_city_name;
        public String in_color_name;
        public String out_color_name;
        public String username;
        public int time;
        public String time_pre;
    }
}
