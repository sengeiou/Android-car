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
public class LimitDiscountEveModel {
    public Pagination pagination;

    public List<DataList> list;
    @JsonProperty("factory_list")
    public FactoryList factoryList;
    @JsonProperty("event")
    public Event event;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {
        @JsonProperty("benefit_id")
        public int benefitId;

        public String type;

        public String logo;
        @JsonProperty("factory_name")
        public String factoryName;

        public String cover;
        @JsonProperty("benefit_attr")
        public List<BenefitAttr> benefitAttr;

        public String title;
        @JsonProperty("apply_begin_time")
        public String applyBeginTime;
        @JsonProperty("apply_end_time")
        public String applyEndTime;
        @JsonProperty("apply_status")
        public int applyStatus;
        @JsonProperty("current_time")
        public String currentTime;
        @JsonProperty("rate_type")
        public int rateType;
        @JsonProperty("rate_value")
        public String rateValue;
        @JsonProperty("sale_num")
        public String saleNum;

        public String number;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FactoryList {
        public List<FactoryListS> list;

        public int count;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FactoryListS {
        @JsonProperty("factory_id")
        public int factoryId;

        public String logo;

        public boolean IsSelect = false;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BenefitAttr {
        public String key;

        public String value;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Event {
        @JsonProperty("event_id")
        public int eventId;
        @JsonProperty("event_name")
        public String eventName;
        @JsonProperty("cover")
        public String cover;
    }

}
