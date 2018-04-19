package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @anthor noti
 * @time 2017/8/24
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LimitDiscountNewDetailsLimitModel {

    public int id;

    public String title;

    public String cover;
    @JsonProperty("benefit_attr")
    public List<BenefitAttr> benefitAttr;
    @JsonProperty("factory_id")
    public int factoryId;
    @JsonProperty("begin_time")
    public String beginTime;
    @JsonProperty("end_time")
    public String endTime;
    @JsonProperty("benefit_status")
    public int benefitStatus;
    @JsonProperty("current_time")
    public String currentTime;

    public String description;

    public String content;

    public String statement;
    @JsonProperty("factory_name")
    public String factoryName;
    @JsonProperty("factory_logo")
    public String factoryLogo;

    public List<Product> product;
    @JsonProperty("rate_type")
    public int rateType;
    @JsonProperty("max_rate_value")
    public String maxRateValue;
    @JsonProperty("series_id")
    public int seriesId;

    public Org org;
    @JsonProperty("org_list")
    public List<Org> orgList;

    @JsonProperty("is_more")
    public int isMore;
    @JsonProperty("series_name")
    public String seriesName;
    @JsonProperty("org_count")
    public int orgCount;
    @JsonProperty("type")
    public String type;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Product {
        @JsonProperty("car_id")
        public int carId;
        @JsonProperty("series_id")
        public int seriesId;
        @JsonProperty("car_name")
        public String carName;
        @JsonProperty("guide_price")
        public String guidePrice;
        @JsonProperty("series_name")
        public String seriesName;
        @JsonProperty("rate_type")
        public int rateType;
        @JsonProperty("rate_value")
        public String rateValue;
        @JsonProperty("max_rate_value")
        public String maxRateValue;
        @JsonProperty("is_series")
        public int isSeries;

        public String price;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BenefitAttr {
        public String key;

        public String value;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Org {
        public int id;
        @JsonProperty("full_name")
        public String fullName;

        public String address;

        public String tel;

        public String distance;
    }

}


