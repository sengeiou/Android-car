package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/16 0016
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripAroundOrgModel {
    @JsonProperty("org_list")
    public OrgList modelList;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrgList{
        @JsonProperty("list")
        public ArrayList<Org> orgList;
        public Pagination pagination;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Org{
        public String name;
        public String longitude;
        public String latitude;
        public String logo;
        public String tel;
        @JsonProperty("service_score")
        public int serviceScore;
        public String address;
        public int id;
        public int distance;
    }
}
