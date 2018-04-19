package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/4/6 0006
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PopmanEslistModel {
    public Pagination pagination;
    @JsonProperty("list")
    public ArrayList<PopmanEsItem> popmanModelist;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class PopmanEsItem{
        public int id;
        public String title;
        public String cover;
        public int reply_count;
        public int view_count;
        public int digg_count;
        public int like_count;
        public int is_digest;
        public String create_time;
    }
}
