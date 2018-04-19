package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/1/17 16:33
 * E-mail：fishloveqin@gmail.com
 */

public class MotoModel {

    @JsonProperty("list")
    public List<CarBean> motos;

    @JsonProperty("customMade")
    public CustomData customData;

    public static class CustomData {

        @JsonProperty("brand")
        public List<Brand> brands;

        @JsonProperty("cc")
        public CC          cc;
        @JsonProperty("price")
        public Price       price;
    }

    public static class CC {

        public String max;
        public String min;
    }

    public static class Price {
        public int max;
        public int min;
    }
}
