package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/1/18 16:46
 * E-mail：fishloveqin@gmail.com
 */

public class MotorshowModel {

    @JsonProperty("list")
    public List<BrandBean> brands;

    public static class BrandBean {
        @JsonProperty("carlist")
        public List<Motorshow> motorshows;
        @JsonProperty("merge_brand_name")
        public String          brandName;
        @JsonProperty("merge_logo")
        public String          brandLogo;

        @JsonProperty("booth_name")
        public String          boothName;

        @JsonProperty("hall_id")
        public String          hallId;
    }

    @JsonProperty("hall")
    public List<Hall> halls;

    public Pagination pagination;
}
