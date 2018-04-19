package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/1/11 15:20
 * E-mail：fishloveqin@gmail.com
 * 新车发布
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewCarBean extends CarBean {

    @JsonProperty("release_time")
    public String      releaseTime;

    //到场明星
    public String      star;

    @JsonProperty("now_time")
    public String      currentTime;

    @JsonProperty("product_id")
    public int         productId;

    @JsonProperty("is_new_brand")
    public int         isNewBrand;

    @JsonProperty("brand_id")
    public int         brandid;
    @JsonProperty("product_name")
    public String         productName;
    @JsonProperty("brand_price")
    public String         brandPrice;
    @JsonProperty("img_list")
    public List<Image> imgs;


}
