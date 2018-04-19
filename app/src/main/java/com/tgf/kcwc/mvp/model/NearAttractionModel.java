package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/10 0010
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class NearAttractionModel {

    public Pagination pagination;
    public ArrayList<NearAttractionItem> list;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NearAttractionItem {
        public String name;
        public String longitude;
        public String latitude;
        public String logo;
        public String tel;
        @JsonProperty("service_score")
        public int    serviceScore;
        public String address;
        public int    id;
        public double distance;
    }
}
