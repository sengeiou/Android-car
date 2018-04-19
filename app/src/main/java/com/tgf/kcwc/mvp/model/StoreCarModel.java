package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/4/11 16:57
 * E-mail：fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreCarModel {

    public Pagination         pagination;//pagination
    public List<StoreCarBean> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StoreCarBean {
        /**
         * benefit : 30312
         * series_name : 测试内容xptl
         * appearance_img :
         * interior_img :
         * appearance_color_name : 白色
         * interior_color_name : 黑色
         * car_name : 宽履 500 LX雪地车
         * id : 6
         */

        public int    benefit;
        @JsonProperty("series_name")
        public String seriesName;
        @JsonProperty("appearance_img")
        public String appearanceImg;
        @JsonProperty("interior_img")
        public String interiorImg;
        @JsonProperty("appearance_color_name")
        public String appearanceColorName;
        @JsonProperty("interior_color_name")
        public String interiorColorName;
        @JsonProperty("car_name")
        public String carName;
        public int    id;
    }
}
