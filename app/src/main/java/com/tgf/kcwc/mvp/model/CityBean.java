package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityBean {

    public int code;

    public String msg;

    public Data data;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public List<Select> list;

        public List<Select> hot;

        public Select select;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Select {
        public int id;

        public String name;

        public int pid;

        public String namecode;

        public String adcode;

        public String alias;

        public boolean  IsSelect=false;
    }

}
