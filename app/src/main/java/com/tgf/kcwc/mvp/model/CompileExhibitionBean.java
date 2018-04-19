package com.tgf.kcwc.mvp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2017/5/4.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompileExhibitionBean {
    @JsonProperty("code")
    public int    code;
    @JsonProperty("msg")
    public String msg;

    @JsonProperty("data")
    public Data   data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public int auth;

        public Detail detail;

        public Event event;

        public List<FactoryList> factory_list ;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Detail {

        public int id;
        @JsonProperty("event_id")
        public int eventId;
        @JsonProperty("factory_id")
        public int factoryId;
        @JsonProperty("series_id")
        public int seriesId;
        @JsonProperty("product_id")
        public int productId;
        @JsonProperty("hall_id")
        public int hallId;
        @JsonProperty("booth_id")
        public int boothId;
        @JsonProperty("product_name")
        public String productName;
        @JsonProperty("appearance_img")
        public String appearanceImg;
        @JsonProperty("appearance_color_name")
        public String appearanceColorName;
        @JsonProperty("appearance_color_value")
        public String appearanceColorValue;
        @JsonProperty("interior_img")
        public String interiorImg;
        @JsonProperty("interior_color_name")
        public String interiorColorName;
        @JsonProperty("interior_color_value")
        public String interiorColorValue;
        @JsonProperty("post_user_id")
        public int postUserId;
        @JsonProperty("post_time")
        public String postTime;
        @JsonProperty("review_user_id")
        public int reviewUserId;
        @JsonProperty("review_time")
        public String reviewTime;
        @JsonProperty("vehicle_type")
        public String vehicleType;

        public int status;
        @JsonProperty("is_post_user")
        public int isPostUser;
        @JsonProperty("factory_name")
        public String factoryName;
        @JsonProperty("post_nickname")
        public String postNickname;
        @JsonProperty("post_real_name")
        public String postRealName;
        @JsonProperty("review_nickname")
        public String reviewNickname;
        @JsonProperty("review_real_name")
        public String reviewRealName;
        @JsonProperty("series_name")
        public String seriesName;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FactoryList {
        @JsonProperty("brand_id")
        public int brandId;
        @JsonProperty("brand_name")
        public String brandName;
        @JsonProperty("hall_id")
        public int hallId;
        @JsonProperty("booth_id")
        public int boothId;

        public String letter;
        @JsonProperty("brand_logo")
        public String brandLogo;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Operation  {
        public String behaviour;

        public String operator;

        public String time;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Event   {
        @JsonProperty("event_id")
        public int eventId;

        public String name;

    }
}
