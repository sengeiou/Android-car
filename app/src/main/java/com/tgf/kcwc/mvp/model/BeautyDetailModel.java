package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/1/17 0017
 * E-mail:hekescott@qq.com
 */

public class BeautyDetailModel {
    @JsonProperty("user_id")
    public int id;
    @JsonProperty("cover")
    public String   cover;
    @JsonProperty("name")
    public String   name;
    @JsonProperty("height")
    public int      height;
    @JsonProperty("bust")
    public int      bust;
    @JsonProperty("waist")
    public int      waist;
    @JsonProperty("hipline")
    public int      hipline;
    @JsonProperty("prize")
    public String   prize;
    @JsonProperty("brand_name")
    public String   brand_name;
    @JsonProperty("brand_logo")
    public String   brandLogo;
    @JsonProperty("hall_name")
    public String   hallName;
    @JsonProperty("booth_name")
    public String   boothName;
    @JsonProperty("img")
    public ImgStore imgStore;

    public static class ImgStore {
        @JsonProperty("list")
        public ArrayList<ImgItem> imgList;
        @JsonProperty("pagination")
        public Pagination         pagination;

    }


    public static class Pagination {
        @JsonProperty("count")
        public int count;
        @JsonProperty("page")
        public int page;
    }

}
