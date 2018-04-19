package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/1/18 0018
 * E-mail:hekescott@qq.com
 */

public class EventListModel {
    @JsonProperty("list")
    public ArrayList<ExhibitEvent> exhibitEventlist;
    @JsonProperty("pagination")
    public EventListModel.Pagination pagination;
    public static class Pagination {
        @JsonProperty("count")
        public int count;
        @JsonProperty("page")
        public int page;
    }

}
