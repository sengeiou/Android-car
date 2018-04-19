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
public class DiscountGiftModel {

    public List<DiscountGiftItem> list;
    public static class DiscountGiftItem {
        @JsonProperty("car_id")
        public int carId;
        public String car_name;
        public String guide_price;
        public int factory_id;
        public String car_series_name;
        public String cover;
        public int org_id;
        public String org_name;
        public int pri_id;
        public String end_time;
        public String title;
        public String desc;
        public int rate;
        public String factory_name;
        public String car_series_img;
        public String create_time;
        public int special;
        public int org_count;
        @JsonProperty("org")
        public List<DiscountLimitModel.LimitOrg> giftOrgs;
    }



}
