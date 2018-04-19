package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BannerModel {
    public int code;

    public String msg;

    public List<Data> data;


    public static class Data {
        public int id;

        public int type;

        public String image;

        public String title;

        public String desc;

        public String module;
        @JsonProperty("obj_id")
        public int objId;

        public String url;

    }

}

