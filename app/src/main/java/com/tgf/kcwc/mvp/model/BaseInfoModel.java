package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * @anthor noti
 * @time 2017/8/15
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseInfoModel implements Serializable{
    @JsonProperty("auth")
    public ArrayList<AuthItem> authItem;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AuthItem implements Serializable{
        @JsonProperty("create_time")
        public String createTime;
        public int id;
        public String photo;
        @JsonProperty("product_id")
        public int productId;
        @JsonProperty("product_name")
        public String productName;
    }
    public String birthday;
    public String company;
    public String department;
    @JsonProperty("home_address")
    public String homeAddress;
    public int id;
    @JsonProperty("is_doyen")
    public int isDoyen;
    @JsonProperty("is_master")
    public int isMaster;
    @JsonProperty("is_model")
    public int isModel;
    public String latitude;
    public String longitude;
    public String name;
    public String position;
    public String qq;
    public String remark;
    @JsonProperty("s_address")
    public String sAddress;
    public String tel;
    @JsonProperty("user_id")
    public int userId;
    public String wechat;
    public String weibo;
    public int sex;
    public int age;
    public int status;
    public String avatar;
}
