package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/14
 * @describle
 */
public class RecordSeeCarModel {
    @JsonProperty("list")
    public ArrayList<ListItem> list;

    @JsonProperty("user")
    public RecordSeeCarModel.UserItem userItem;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListItem{
        @JsonProperty("order_offer")
        public OrderOfferItem orderOfferItem;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class OrderOfferItem{
            @JsonProperty("is_mine")
            public int isMine;
            @JsonProperty("is_offer")
            public int isOffer;
            @JsonProperty("offer_status")
            public int offerStatus;
            @JsonProperty("order_id")
            public int orderId;
            @JsonProperty("saler_name")
            public String salerName;
        }
        @JsonProperty("appearance_color")
        public String appearanceColor;
        @JsonProperty("car_id")
        public int carId;
        @JsonProperty("car_name")
        public String carName;
        @JsonProperty("create_time")
        public String createTime;
        public String description;
        public int id;
        @JsonProperty("interior_color")
        public String interiorColor;
        @JsonProperty("order_sn")
        public String orderSn;
        @JsonProperty("series_id")
        public int seriesId;
        @JsonProperty("series_name")
        public String seriesName;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserItem{
        public String avatar;
        public String birthday;
        public int id;
        @JsonProperty("is_doyen")
        public int isDoyen;
        @JsonProperty("is_master")
        public int isMaster;
        @JsonProperty("is_model")
        public int isModel;
        public String logo;
        public String nickname;
        public int age;
        public int sex;
        public String tel;
        @JsonProperty("user_id")
        public int userId;
    }
}
