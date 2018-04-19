package com.tgf.kcwc.mvp.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/1/20 0020
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExhibitionPicsModel {
    @JsonProperty("lists")
    public ArrayList<ImgItem> exhibitImgeList;
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public static class ExhibitImge {
//        @JsonProperty("id")
//        public int    id;
//        @JsonProperty("link_url")
//        public String linkUrl;
//        @JsonProperty("name")
//        public String name;
//    }
}
