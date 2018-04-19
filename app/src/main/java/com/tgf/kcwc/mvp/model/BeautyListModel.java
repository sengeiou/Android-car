package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2016/12/29 0029
 * E-mail:hekescott@qq.com
 */

public class BeautyListModel {
    //场馆下有哪些品牌哪些模特
    public List<Area> beautyArea;
    @JsonProperty("data")
    public Data       data;
    @JsonProperty("pagination")
    public Pagination pagination;
    public static class Data {
        @JsonProperty("brand_list")
        public ArrayList<BeautyBrand> beautyBranList;
        @JsonProperty("event_name")
        public String       exhibitName;
        @JsonProperty("event_id")
        public int       eventId;
    }
    public  static  class BeautyBrand{
        public int id;
        public String name;
        public String logo;
        public String letter;
        @JsonProperty("model_list")
        public List<Beauty> beautyList;
    }


}
