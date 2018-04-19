package com.tgf.kcwc.mvp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2017/4/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicDetailsBean {
    public int    code;

    public String msg;

    public Data   data;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public int id;

        public String title;

        public String intro;

        public String cover;

        @JsonProperty("is_posid")
        public int isPosid;

        @JsonProperty("fans_num")
        public int fansNum;

        @JsonProperty("create_time")
        public String createTime;

        @JsonProperty("is_attention")
        public int isAttention;

        @JsonProperty("is_carousel")
        public int isCarousel;

        @JsonProperty("is_compere")
        public int isCompere;

        @JsonProperty("thread_num")
        public int threadNum;

        @JsonProperty("is_login")
        public int isLogin;

        @JsonProperty("create_user")
        public CreateUser createUser;

        @JsonProperty("compere_user")
        public CompereUser compereUser;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateUser {
        public int id;

        public String nickname;

        public String tel;

    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CompereUser {
        public int id;

        public String nickname;

        public String avatar;

    }

}
