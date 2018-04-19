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
public class LimitDiscountNewDetailsModel {

    public int id;
    @JsonProperty("event_id")
    public int eventId;

    public String title;

    public String cover;
    @JsonProperty("benefit_attr")
    public List<BenefitAttr> benefitAttr;
    @JsonProperty("factory_id")
    public int factoryId;
    @JsonProperty("apply_begin_time")
    public String applyBeginTime;
    @JsonProperty("apply_end_time")
    public String applyEndTime;
    @JsonProperty("apply_status")
    public int applyStatus;
    @JsonProperty("current_time")
    public String currentTime;

    public Sale sale;

    public String description;

    public String content;

    public String statement;

    public List<Product> product;

    public Event event;
    @JsonProperty("factory_name")
    public String factoryName;
    @JsonProperty("factory_logo")
    public String factoryLogo;

    public List<Exhibitor> exhibitor;

    public String number;

    public String type;
    @JsonProperty("series_id")
    public String seriesId;
    @JsonProperty("series_name")
    public String seriesName;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Product {
        public int id;
        public String name;
        @JsonProperty("rate_type")
        public int rateType;
        @JsonProperty("rate_value")
        public String rateValue;
        public String title;
        @JsonProperty("is_check")
        public int isCheck;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BenefitAttr {
        public String key;

        public String value;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sale {
        public String title;
        @JsonProperty("car_name")
        public String carName;
        @JsonProperty("series_name")
        public String seriesName;
        @JsonProperty("is_series")
        public int isSeries;
        @JsonProperty("product_id")
        public int productId;
        @JsonProperty("guide_price")
        public String guidePrice;

        public String price;
        @JsonProperty("rate_type")
        public int rateType;
        @JsonProperty("rate_value")
        public String rateValue;
        @JsonProperty("sale_num")
        public String saleNum;

        public String config;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Exhibitor {
        public int id;
        @JsonProperty("full_name")
        public String fullName;

        public String address;

        public String tel;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Event {
        public int id;

        public String name;
        @JsonProperty("start_time")
        public String startTime;
        @JsonProperty("end_time")
        public String endTime;

        public String address;
        @JsonProperty("factory_logo")
        public String factoryLogo;
        @JsonProperty("factory_name")
        public String factoryName;
        @JsonProperty("hall_id")
        public int hallId;
        @JsonProperty("hall_name")
        public String hallName;
        @JsonProperty("booth_id")
        public int boothId;
        @JsonProperty("booth_name")
        public String boothName;
        @JsonProperty("cover")
        public String cover;

    }

}


