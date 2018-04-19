package com.tgf.kcwc.mvp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2017/5/4.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreExhibitionBean {
    @JsonProperty("code")
    public int    code;
    @JsonProperty("msg")
    public String msg;

    @JsonProperty("data")
    public Data   data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public int            auth;

        public Event          event;

        public Pagination     pagination;

        public List<DataList> list;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {

        public int    id;
        @JsonProperty("factory_id")
        public int    factoryId;
        @JsonProperty("series_id")
        public int    seriesId;
        @JsonProperty("series_name")
        public String seriesName;
        @JsonProperty("product_id")
        public int    productId;
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
        public int    postUserId;
        @JsonProperty("post_time")
        public String postTime;
        @JsonProperty("review_user_id")
        public int    reviewUserId;
        @JsonProperty("review_time")
        public String reviewTime;
        @JsonProperty("vehicle_type")
        public String vehicleType;

        public int    status;
        @JsonProperty("is_post_user")
        public int    isPostUser;
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

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Event {
        public int    id;

        public String name;

        public String cover;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PostUser {
        public int    id;

        public String nickname;
        @JsonProperty("real_name")
        public String realName;
        @JsonProperty("post_time")
        public String postTime;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReviewUser {
        public int    id;

        public String nickname;
        @JsonProperty("real_name")
        public String realName;
        @JsonProperty("review_time")
        public String reviewTime;
    }
}
