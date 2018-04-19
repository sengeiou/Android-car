package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/1/18 16:06
 * E-mail：fishloveqin@gmail.com
 */

public class Motorshow extends CarBean {

    @JsonProperty("car_img_list")
    public Image       imgList;

    @JsonProperty("booth_name")
    public String      boothName;

    @JsonProperty("hall_name")
    public String      hallName;
    @JsonProperty("area_name")
    public String      areaName;
    @JsonProperty("product_id")
    public int         productId;
    @JsonProperty("product_name")
    public String      productName;
    @JsonProperty("booth_id")
    public String      boothId;
    @JsonProperty("list_img")
    public List<Image> images;

    @JsonProperty("is_series")
    public int         isSeries;
}
