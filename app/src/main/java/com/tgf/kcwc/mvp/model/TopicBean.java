package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/5/13.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicBean {
    public int code;

    public String msg;

    public Data data;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data{
        public Pagination pagination;

        public List<DataList> list ;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList{
        public int id;

        public String title;

        public String intro;

        public String cover;
        @JsonProperty("is_posid")
        public int isPosid;
        @JsonProperty("fans_num")
        public int fansNum;

        public int status;
        @JsonProperty("create_time")
        public String createTime;

        public int order;
        @JsonProperty("thread_num")
        public int threadNum;
        @JsonProperty("create_user")
        public CreateUser createUser;
        @JsonProperty("compere_user")
        public CompereUser compereUser;

        public Category category;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateUser{
        public int id;

        public String nickname;

        public String tel;

    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CompereUser{
        public int id;

        public String nickname;

        public String tel;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Category{
        public int id;

        public String name;
    }

}
