package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/25 0025
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScanOffDetailModel {
    public ArrayList<ScanOffDetailItem> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ScanOffDetailItem {
        public int    id;
        public String avatar;
        public String nickname;
        public int    sex;
        @JsonProperty("check_time")
        public String checkTime;
    }

}
