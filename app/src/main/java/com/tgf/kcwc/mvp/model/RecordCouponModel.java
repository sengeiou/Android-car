package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/14
 * @describle
 */
public class RecordCouponModel {
    @JsonProperty("list")
    public ArrayList<RecordCouponModel.ListItem> list;

    @JsonProperty("user")
    public RecordCouponModel.UserItem userItem;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListItem{
        @JsonProperty("code_list")
        public ArrayList<CodeItem> codeItems;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class CodeItem{
            @JsonProperty("check_id")
            public int checkId;
            @JsonProperty("is_receive")
            public int isReceive;
            @JsonProperty("check_time")
            public String checkTime;
            public String code;
            @JsonProperty("receive_time")
            public String receiveTime;
        }
        @JsonProperty("coupon_total")
        public int couponTotal;
        @JsonProperty("coupon_type")
        public String couponType;
        @JsonProperty("create_time")
        public String createTime;
        public int id;
        @JsonProperty("is_mine")
        public int isMine;
        @JsonProperty("order_sn")
        public String orderSn;
        @JsonProperty("saler_name")
        public String salerName;
        public String title;
        @JsonProperty("total_price")
        public String totalPrice;
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
        public int sex;
        public String tel;
        @JsonProperty("user_id")
        public int userId;
        public int age;
    }
}
