package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/2/9 15:17
 * E-mail：fishloveqin@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicDetailsBean {

    public int id;

    public String title;
    @JsonProperty("reply_count")
    public int replyCount;
    @JsonProperty("view_count")
    public int viewCount;
    @JsonProperty("digg_count")
    public int diggCount;
    @JsonProperty("like_count")
    public int likeCount;
    @JsonProperty("is_digest")
    public int isDigest;
    @JsonProperty("create_time")
    public String createTime;

    public String longitude;

    public String latitude;
    @JsonProperty("local_address")
    public String localAddress;
    @JsonProperty("is_creator")
    public int isCreator;

    public String distance;
    @JsonProperty("is_collect")
    public int isCollect;
    @JsonProperty("is_follow")
    public int isFollow;
    @JsonProperty("create_user")
    public CreateUser createUser;
    @JsonProperty("is_praise")
    public int isPraise;

    public List<Topic> topic;

    public Mood mood;

    public static class CreateUser {
        public int id;

        public String nickname;

        public String avatar;

        public int sex;

        public int age;
        @JsonProperty("is_doyen")
        public int isDoyen;
        @JsonProperty("is_model")
        public int isModel;
        @JsonProperty("is_master")
        public int isMaster;
        @JsonProperty("master_brand")
        public String masterBrand;

    }

    public static class Mood {
        public String content;

        public String image;

        public String video;
    }

}
