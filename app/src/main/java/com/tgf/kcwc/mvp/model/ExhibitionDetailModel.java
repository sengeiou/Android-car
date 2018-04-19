package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/1/16 0016
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExhibitionDetailModel {
    @JsonProperty("exhibition_list")
    public Exhibition exhibition;
    @JsonProperty("exhibition_navigation")
    public ArrayList<MenuItem> menuList;
    @JsonProperty("position_position_img")
    public ArrayList<ExhibitPlace> exhibitPlacelist;
    @JsonProperty("plink_list")
    public ArrayList<String> pinkImgs;
    @JsonProperty("cover_list")
    public ArrayList<String> coverlist;
    @JsonProperty("is_jump")
    public int isJump;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MenuItem {
        @JsonProperty("id")
        public int    id;
        @JsonProperty("link")
        public String link;
        public String iconurl;
        @JsonProperty("name")
        public String title;
        @JsonProperty("url")
        public String url;
    }
}
