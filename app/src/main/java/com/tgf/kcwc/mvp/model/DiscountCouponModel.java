package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/4/25 0025
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscountCouponModel {

    public List<DiscountCouponItem> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DiscountCouponItem {
        public int    id;
        public String title;
        public String cover;
        public String desc;
        public String price;
        public String denomination;
        @JsonProperty("send_total")
        public int    sendTotal;
        @JsonProperty("putaway_time")
        public String putawayTime;
        @JsonProperty("soldout_time")
        public String soldoutTime;
        public int    sale;
        public double discount;
        public int    meter;
        public int    special;
        public int    source;
    }

}
