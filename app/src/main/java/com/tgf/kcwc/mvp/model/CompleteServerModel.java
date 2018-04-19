package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/27 0027
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CompleteServerModel {

    @JsonProperty("brand_name")
    public String brand_name;
    @JsonProperty("car_name")
    public String car_name;
    @JsonProperty("car_cover")
    public String car_cover;
    @JsonProperty("user_id")
    public int user_id;
    @JsonProperty("id")
    public int id;

    @JsonProperty("status")
    public int status;
    @JsonProperty("offer_list")
    public ArrayList<OrgItem> offer_list;
    @JsonProperty("offer_count")
    public int offer_count;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrgItem {

        @JsonProperty("avatar")
        public String avatar;
        @JsonProperty("msg_num")
        public int    msg_num;
        @JsonProperty("nickname")
        public String nickname;
        @JsonProperty("star")
        public String star;
        @JsonProperty("offer")
        public String offer;
        @JsonProperty("org_name")
        public String org_name;
        @JsonProperty("org_address")
        public String org_address;
        @JsonProperty("id")
        public int    id;
        @JsonProperty("offer_user_id")
        public int    offer_user_id;
        @JsonProperty("comment_star")
        public int    comment_star;

    }
}
