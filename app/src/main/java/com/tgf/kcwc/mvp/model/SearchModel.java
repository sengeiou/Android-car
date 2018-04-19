package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/18
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchModel {
    @JsonProperty("factory")
    public ArrayList<SearchModel.FactoryItem> factoryItems;
    @JsonProperty("price")
    public ArrayList<SearchModel.PriceItem> priceItems;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FactoryItem {
        @JsonProperty("factory_name")
        public String factoryName;
        public double percent;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PriceItem {
        @JsonProperty("price_range")
        public String priceRange;
        public double percent;

        @Override
        public String toString() {
            return "PriceItem{" +
                    "priceRange='" + priceRange + '\'' +
                    ", percent=" + percent +
                    '}';
        }
    }
}
