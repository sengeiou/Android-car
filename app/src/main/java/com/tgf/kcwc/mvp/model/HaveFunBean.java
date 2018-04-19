package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HaveFunBean {
    public int code;

    public String msg;

    public Data data;

    public static class Data {
        public Pagination pagination;

        public List<DataList> list;

    }

    public static class DataList {
        public int id;

        public String title;

        public String model;

        public String cover;
        @JsonProperty("reply_count")
        public int replyCount;
        @JsonProperty("view_count")
        public int viewCount;
        @JsonProperty("digg_count")
        public int diggCount;
        @JsonProperty("is_digest")
        public int isDigest;
        @JsonProperty("create_time")
        public String createTime;

    }

}
