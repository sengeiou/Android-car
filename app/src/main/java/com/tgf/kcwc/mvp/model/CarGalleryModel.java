package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/1/17 19:05
 * E-mail：fishloveqin@gmail.com
 */

public class CarGalleryModel {

    public int        id;

    public String     name;
    @JsonProperty("brand_name")
    public String     brandName;
    @JsonProperty("booth_name")
    public String     boothName;
    @JsonProperty("hall_name")
    public String     hallName;
    @JsonProperty("brand_logo")
    public String     logo;

    @JsonProperty("img_list")
    public ImageModel model;

    public static class ImageModel {
        @JsonProperty("pagination")
        public Pagination  pagination;
        @JsonProperty("list")
        public List<Image> images;
    }

    @JsonProperty("series_name")
    public String seriesName;

    @JsonProperty("relation")
    public int    relation;

    @JsonProperty("series_id")
    public int    seriesId;

    @JsonProperty("product_id")
    public int    productId;

    @JsonProperty("product_name")
    public String productName;
}
