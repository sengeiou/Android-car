package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.util.URLUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/12.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayListBean {
    public int code;

    public String msg;

    public Data data;

    public static class Data{
        public Pagination pagination;

        public List<DataList> list ;
        @JsonProperty("is_relevance")
        public int isRelevance;
    }

    public static class DataList{
        public int id;

        public List<String> cover ;

        public String model;

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
        @JsonProperty("view_type")
        public int viewType;
        @JsonProperty("create_time")
        public String createTime;

        private List<String>    listImgs = new ArrayList<>();

        public List<String> getImglist() {
            if (cover != null && listImgs.size() == 0) {
                for (String img : cover) {
                    listImgs.add(URLUtil.builderImgUrl(img, 360, 360));
                }
            }
            return listImgs;
        }
    }


}
