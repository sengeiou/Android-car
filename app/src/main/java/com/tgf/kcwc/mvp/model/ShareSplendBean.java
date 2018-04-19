package com.tgf.kcwc.mvp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2017/5/2.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShareSplendBean {
    public int    code;

    public String msg;

    public Data   data;

    public static class Data {
        @JsonProperty("create_by")
        public int            createBy;

        public Threads        thread;

        public Pagination     pagination;

        public List<DataList> list;
    }

    public static class Threads {
        public int    id;

        public String title;
        @JsonProperty("begin_time")
        public String beginTime;
        @JsonProperty("end_time")
        public String endTime;
        @JsonProperty("create_by")
        public int    createBy;

        public int    num;

    }

    public static class DataList {
        public int      id;

        public String   title;

        public String   cover;
        @JsonProperty("create_by")
        public CreateBy createBy;
        @JsonProperty("is_digest")
        public int      isDigest;
        @JsonProperty("reply_count")
        public int      replyCount;
        @JsonProperty("like_count")
        public int      likeCount;
        @JsonProperty("digg_count")
        public int      diggCount;
    }

    public static class CreateBy {
        public int    id;

        public String username;

        public String avatar;

        public int    sex;
        @JsonProperty("is_doyen")
        public int    isDoyen;
        @JsonProperty("is_model")
        public int    isModel;
        @JsonProperty("is_master")
        public int    isMaster;

        public String logo;
    }
}
