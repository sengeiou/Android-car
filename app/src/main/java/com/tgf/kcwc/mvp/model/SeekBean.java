package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeekBean {
    public int code;

    public String msg;

    public Data data;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public int total;

        public List<DataList> list;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {
        public int id;

        public String index;

        public String type;

        public String name;

        public String title;
    }

}
