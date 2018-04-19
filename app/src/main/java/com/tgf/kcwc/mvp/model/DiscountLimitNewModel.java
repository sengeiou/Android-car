package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/4/25 0025
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscountLimitNewModel {

    public Pagination pagination;
    @JsonProperty("list")
    public List<LimitDiscountItem> mLimitDiscountItemList;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LimitOrg {
        public int id;

        public String name;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BenefitAttr {
        public String key;

        public String value;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LimitDiscountItem {
        @JsonProperty("id")
        public int id;
        @JsonProperty("factory_name")
        public String factoryName;
        @JsonProperty("factory_logo")
        public String factoryLogo;

        public String cover;

        public String type;

        @JsonProperty("benefit_attr")
        public List<BenefitAttr> benefitAttr;

        public String title;
        @JsonProperty("begin_time")
        public String beginTime;
        @JsonProperty("end_time")
        public String endTime;

        public List<LimitOrg> org;

        @JsonProperty("benefit_status")
        public int benefitStatus;

        @JsonProperty("current_time")
        public String currentTime;

    }
}
