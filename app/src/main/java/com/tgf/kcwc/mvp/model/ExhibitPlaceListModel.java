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
public class ExhibitPlaceListModel {
    @JsonProperty("total_img")
    public String totalImg;
    @JsonProperty("position_img")
    public ArrayList<ExhibitPlace> exhibitPlaceList;
}
