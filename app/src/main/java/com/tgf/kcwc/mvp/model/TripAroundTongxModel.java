package com.tgf.kcwc.mvp.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/5/16 0016
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripAroundTongxModel {
    @JsonProperty("along_list")
    public TongXList modelList;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TongXList{
        @JsonProperty("list")
        public ArrayList<TongX> tongxList;
        public Pagination pagination;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TongX{
        public String nickname;
        public String avatar;
        public String lng;
        public String lat;
        @JsonProperty("add_time")
        public String addTime;
        public int distance;
        public int sex;
    }
}
