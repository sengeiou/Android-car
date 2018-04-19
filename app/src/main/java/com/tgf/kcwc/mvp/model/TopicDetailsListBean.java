package com.tgf.kcwc.mvp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2017/4/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicDetailsListBean {
    public int code;

    public String msg;

    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public Pagination pagination;

        public List<DataList> list;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {

        public int id;

        @JsonProperty("sender_user")
        public List<senderUser> senderUser;

        @JsonProperty("thread_id")
        public int thread_id;
        @JsonProperty("topic_id")
        public int topicId;
        @JsonProperty("is_top")
        public int isTop;
        @JsonProperty("create_time")
        public String createTime;

        public Thread thread;
        @JsonProperty("create_user")
        public CreateUser createUser;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateUser {
        public int id;

        public String nickname;

        public String avatar;

        public int sex;
        @JsonProperty("is_model")
        public int isModel;
        @JsonProperty("is_doyen")
        public int isDoyen;
        @JsonProperty("is_master")
        public int isMaster;
        @JsonProperty("master_brand")
        public String masterBrand;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Thread {
        public int id;

        public String title;

        public String cover;
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

        public String model;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class senderUser {
        @JsonProperty("nickname")
        public String nickname;
        @JsonProperty("create_time")
        public String createTime;

    }

}
