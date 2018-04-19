package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/15 0015
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TripBookThreadModel {
    public String                     adds;
    @JsonProperty("thread_list")
    public ThreadListModel threadList;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ThreadListModel {
        public Pagination pagination;
        public ArrayList<Thread>     list;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Thread {
        public int    id;
        public String model;
        public String title;
        public String cover;
        @JsonProperty("reply_count")
        public int    replyCount;
        @JsonProperty("view_count")
        public int    viewCount;
        @JsonProperty("digg_count")
        public int    diggCount;
        @JsonProperty("like_count")
        public int    likeCount;
        @JsonProperty("share_count")
        public int    shareCount;
        @JsonProperty("is_digest")
        public int    isDigest;
        @JsonProperty("create_time")
        public String createTime;
    }
}
