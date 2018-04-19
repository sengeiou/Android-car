package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/1/19 0019
 * E-mail:hekescott@qq.com
 * 分页的基类
 */

public class BasePageModel {
    @JsonProperty("pagination")
    public Pagination pagination;

    public static class Pagination {
        @JsonProperty("count")
        public int count;
        @JsonProperty("page")
        public int page;
    }
}
