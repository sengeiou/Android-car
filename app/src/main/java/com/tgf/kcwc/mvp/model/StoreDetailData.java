package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * Author：Jenny
 * Date:2016/12/22 18:50
 * E-mail：fishloveqin@gmail.com
 * 店铺详情
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreDetailData {

    @JsonProperty("latitude")
    public String latitude;
    @JsonProperty("longitude")
    public String longitude;
    @JsonProperty("org_score")
    public int compositeScore;
    @JsonProperty("address")
    public String address;
    @JsonProperty("env_score")
    public int envScore;
    @JsonProperty("banner_count")
    public String bannerNum;
    @JsonProperty("service_score")
    public int serviceScore;
    @JsonProperty("org_logo")
    public String logo;
    @JsonProperty("org_name")
    public String name;
    @JsonProperty("tel")
    public String tel;
    @JsonProperty("org_id")
    public String id;
    @JsonProperty("active_list")
    public List<Topic> topics;
    @JsonProperty("banner_list")
    public List<Banner> banners;

    @JsonProperty("org_user")
    public List<Account.UserInfo> users;

    public String banner;

    @JsonProperty("is_collect")
    public  int isCollect;
}
