package com.tgf.kcwc.mvp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/4/26 0026
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class LimitDiscountDetailModel {

    public String        cover;
    public String        title;
    public String        desc;
    @JsonProperty("end_time")
    public String       endTime;
    public String          rate;
    public int          rate_type;
    @JsonProperty("car")
    public List<GiftCar> giftCars;

    public static class GiftCar {
        public int    car_id;
        @JsonProperty("car_name")
        public String carName;
        @JsonProperty("guide_price")
        public int    guidePrice;
        @JsonProperty("car_series_name")
        public String carSeriesName;
        public String pic;
        public String factory;
    }

}
