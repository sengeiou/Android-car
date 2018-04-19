package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/24 0024
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScanOffListModel {

    public ArrayList<ScanOffItem> list;

    public static class ScanOffItem {
        @JsonProperty("check_num")
        public int    checkNum;
        @JsonProperty("check_price")
        public String checkPrice;
        @JsonProperty("coupon_id")
        public int    couponId;
        public String title;
        public String cover;
        @JsonProperty("begin_time")
        public String beginTime;
        @JsonProperty("end_time")
        public String endTime;
        public String price;
        public String denomination;
        @JsonProperty("org_name")
        public String orgName;


    }
}
