package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoundTypeBean {
    public int code;

    public String msg;

    public List<Data> data;

    public static class Data {
        public int id;

        public String name;
        @JsonProperty("parent_id")
        public int parentId;
        @JsonProperty("category_type")
        public String categoryType;
    }
}
