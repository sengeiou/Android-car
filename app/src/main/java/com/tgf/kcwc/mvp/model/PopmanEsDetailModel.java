package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/7/13 0013
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PopmanEsDetailModel {
    public int id;
    public String cover;
    public String title;
    public int reply_count;
    public int view_count;
    public int digg_count;
    public int like_count;
    public int is_digest;
    public String create_time;
    public String longitude;
    public String latitude;
    public String local_address;
    public int is_pass;
    public String pass_remark;
    public int is_creator;
    public int is_praise;
    public int is_collect;
    public String distance;
    public EvaluateDate    evaluate;
    @JsonProperty("create_user")
    public CreateUser    createUser;
    @JsonProperty("topic")
    public ArrayList<Topic> topiclist;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EvaluateDate{
        public String content;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateUser{
        public int id;
        public String nickname;
        public String avatar;
        public int sex;
        public int age;
        public int is_doyen;
        public int is_model;
        public int is_master;
        public String master_brand;
    }
}
