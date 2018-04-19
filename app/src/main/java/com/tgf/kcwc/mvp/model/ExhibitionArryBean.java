package com.tgf.kcwc.mvp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2017/5/4.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExhibitionArryBean {
    @JsonProperty("code")
    public int    code;
    @JsonProperty("msg")
    public String msg;

    @JsonProperty("data")
    public List<Data> data ;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public int id;

        public String name;

    }
}
