package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/14
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordTicketModel {
    @JsonProperty("list")
    public ArrayList<ListItem> list;
    @JsonProperty("user")
    public UserItem userItem;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListItem{
        @JsonProperty("record")
        public ArrayList<RecordItem> recordItems;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class RecordItem{
            public String time;
            public String content;
        }
        @JsonProperty("enter_time")
        public String enterTime;
        @JsonProperty("is_enter")
        public int isEnter;
        @JsonProperty("is_receive")
        public int isReceive;
        @JsonProperty("receive_time")
        public String receiveTime;
        @JsonProperty("serial_sn")
        public String serialSn;
        public String name;
        public int status;
//        @JsonProperty("create_time")
//        public String createTime;
//        @JsonProperty("record_type")
//        public String recordType;
//        public String type;
//        public String name;
//        public int id;

        @JsonProperty("order_id")
        public int orderId;
        @JsonProperty("order_sn")
        public String orderSn;
        @JsonProperty("record_type")
        public String recordType;
        @JsonProperty("is_mine")
        public int isMine;
        @JsonProperty("saler_name")
        public String salerName;
        public int num;
        @JsonProperty("create_time")
        public String createTime;
        @JsonProperty("event_name")
        public String eventName;
        public String type;

        @JsonProperty("code_list")
        public ArrayList<CodeListItem> codeListItems;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class CodeListItem{
            @JsonProperty("serial_sn")
            public String serialSn;
            public int status;
            @JsonProperty("is_receive")
            public int isReceive;
            @JsonProperty("is_checkout")
            public int isCheckout;
            @JsonProperty("receive_time")
            public String receiveTime;
            @JsonProperty("checkout_time")
            public String checkoutTime;
        }
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
