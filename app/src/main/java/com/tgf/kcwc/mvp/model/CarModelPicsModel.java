package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/1/20 0020
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarModelPicsModel {
    @JsonProperty("appearance_img")
    public ArrayList<CarModelPic> appearanceImglist;
    @JsonProperty("interior_img")
    public ArrayList<CarModelPic> interiorImglist;
    @JsonProperty("xcimg")
    public ArrayList<CarModelPic> xcImglist;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CarModelPic {
        @JsonProperty("id")
        public int    id;
        @JsonProperty("path")
        public String path;
        @JsonProperty("type")
        public String type;
    }
}
