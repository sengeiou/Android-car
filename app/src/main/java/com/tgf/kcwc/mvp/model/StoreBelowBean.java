package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreBelowBean {
    @JsonProperty("code")
    public int    code;
    @JsonProperty("msg")
    public String msg;

    @JsonProperty("data")
    public Data   data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public int            auth;

        public Store          store;

        public Pagination     pagination;

        public List<DataList> list;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {
        public int        id;
        @JsonProperty("factory_id")
        public int        factoryId;
        @JsonProperty("series_id")
        public int        seriesId;
        @JsonProperty("series_name")
        public String     seriesName;
        @JsonProperty("car_id")
        public int        carId;
        @JsonProperty("car_name")
        public String     carName;
        @JsonProperty("type_exist")
        public int        typeExist;
        @JsonProperty("type_show")
        public int        typeShow;
        @JsonProperty("appearance_img")
        public String     appearanceImg;
        @JsonProperty("appearance_color_name")
        public String     appearanceColorName;
        @JsonProperty("appearance_color_value")
        public String     appearanceColorValue;
        @JsonProperty("interior_img")
        public String     interiorImg;
        @JsonProperty("interior_color_name")
        public String     interiorColorName;
        @JsonProperty("interior_color_value")
        public String     interiorColorValue;
        @JsonProperty("diff_conf")
        public String     diffConf;

        public int        status;
        @JsonProperty("vehicle_type")
        public String     vehicleType;
        @JsonProperty("is_post_user")
        public int        isPostUser;
        @JsonProperty("factory_name")
        public String     factoryName;
        @JsonProperty("post_user")
        public PostUser   postUser;
        @JsonProperty("review_user")
        public ReviewUser reviewUser;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Store {
        public int    id;

        public String name;
        @JsonProperty("full_name")
        public String fullName;

        public String logo;
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
