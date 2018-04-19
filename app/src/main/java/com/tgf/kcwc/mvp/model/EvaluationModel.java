package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/8/7 0007
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class EvaluationModel {

    public Sale sale;
    public ArrayList<Tag> tags;
    public Org org;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sale{
        public int id;
        public String nickname;
        public String avatar;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tag{
        public int id;
        @JsonProperty("tag_name")
        public String tagName;
        public  boolean iSchecked;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Org{
        public int id;
        public String name;
        public String address;
        public int star;
    }
}
