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
public class CustomizedCollectModel {
    @JsonProperty("user")
    public ActionRecordModel.UserItem userItem;
    @JsonProperty("custom_made")
    public CustomizedCollectModel.CustomMadeItem customMadeItem;
    @JsonProperty("collect")
    public CustomizedCollectModel.CollectItem collectItem;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserItem {
        public int id;
        public String tel;
        @JsonProperty("user_id")
        public int userId;
        public String avatar;
        public String name;
        public int sex;
        public String birthday;
        @JsonProperty("is_model")
        public int isModel;
        @JsonProperty("is_master")
        public int isMaster;
        @JsonProperty("is_doyen")
        public int isDoyen;
        public int age;
        public String logo;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomMadeItem {
        @JsonProperty("pagination")
        public Pagination pagination;
        @JsonProperty("list")
        public ArrayList<CustomizedCollectModel.CustomMadeItem.ListItem> listItem;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ListItem {
            @JsonProperty("create_time")
            public String createTime;
            public String level;
            public String countries;
            @JsonProperty("factory_names")
            public String factoryNames;
            @JsonProperty("cc_range")
            public String ccRange;
            @JsonProperty("seat_num_range")
            public String seatNumRange;
            public String price;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CollectItem {
        @JsonProperty("factory")
        public ArrayList<CollectItem.FactoryItem> factoryItems;
        @JsonProperty("series")
        public ArrayList<CollectItem.SeriesItem> seriesItems;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class FactoryItem {
            @JsonProperty("factory_name")
            public String factoryName;
            public double percent;

        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class SeriesItem {
            @JsonProperty("series_name")
            public String seriesName;
            public int number;
        }

    }
}
