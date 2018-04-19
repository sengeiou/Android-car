package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/17
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunityModel {
    @JsonProperty("hobby")
    public ArrayList<HobbyItem> hobbyItem;
    @JsonProperty("keywords")
    public ArrayList<String> keywordsItem;
    @JsonProperty("view")
    public ArrayList<ViewItem> viewItem;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HobbyItem {
        public int id;
        @JsonProperty("parent_id")
        public int parent_id;
        public String name;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ViewItem {

    }
}
