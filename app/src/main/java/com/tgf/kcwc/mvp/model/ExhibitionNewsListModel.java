package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2016/12/28 0028
 * E-mail:hekescott@qq.com
 */

public class ExhibitionNewsListModel {
    @JsonProperty("list")
    public ArrayList<ExhibitionNews> newsList;
    @JsonProperty("pagination")
    public EventListModel.Pagination pagination;

    public static class Pagination {
        @JsonProperty("count")
        public int count;
        @JsonProperty("page")
        public int page;
    }
}
