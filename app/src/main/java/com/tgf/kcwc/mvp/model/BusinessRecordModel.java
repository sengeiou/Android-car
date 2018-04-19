package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @anthor noti
 * @time 2017/8/15
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessRecordModel {
    @JsonProperty("activity")
    public ActivityItem activityItem;
    @JsonProperty("basic_info")
    public BasicInfoItem basicInfoItem;
    @JsonProperty("coupon")
    public CouponItem couponItem;
    @JsonProperty("see")
    public SeeItem seeItem;
    @JsonProperty("ticket")
    public TicketItem ticketItem;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ActivityItem {
        @JsonProperty("cycle")
        public CycleItem cycleItem;
        @JsonProperty("nearest")
        public NearestItem nearestItem;
        @JsonProperty("play")
        public PlayItem playItem;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class CycleItem {
            @JsonProperty("my_total")
            public int myTotal;
            public int total;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class NearestItem {
            public int id;
            public String title;
            public String model;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PlayItem {
            @JsonProperty("my_total")
            public int myTotal;
            public int total;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BasicInfoItem {
        public String avatar;
        public String birthday;
        public int id;
        @JsonProperty("is_doyen")
        public int isDoyen;
        @JsonProperty("is_master")
        public int isMaster;
        @JsonProperty("is_model")
        public int isModel;
        public String logo;
        public String nickname;
        public int sex;
        public String tel;
        @JsonProperty("user_id")
        public int userId;
        public int age;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CouponItem {
        @JsonProperty("buy")
        public BuyItem buyItem;

        @JsonProperty("distribute")
        public DistributeItem distributeItem;

        @JsonProperty("nearest")
        public NearestItem nearestItem;

        @JsonProperty("nearest_check")
        public NearestCheckItem nearestCheckItem;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class BuyItem {
            public int check;
            public int total;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DistributeItem {
            public int check;
            public int total;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class NearestItem {
            public int id;
            public String title;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class NearestCheckItem {
            public int id;
            public String title;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SeeItem {
        @JsonProperty("my_total_halfyear")
        public int myTotalHalfYear;
        @JsonProperty("my_total_month")
        public int myTotalMonth;
        @JsonProperty("my_total_year")
        public int myTotalYear;

        @JsonProperty("nearest")
        public NearestItem nearestItem;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class NearestItem {
            public int id;
            @JsonProperty("car_id")
            public int carId;
            @JsonProperty("series_id")
            public int seriesId;
            @JsonProperty("create_time")
            public String createTime;
            @JsonProperty("car_name")
            public String carName;
            @JsonProperty("series_name")
            public String seriesName;
        }

        @JsonProperty("total_halfyear")
        public int totalHalfYear;
        @JsonProperty("total_month")
        public int totalMonth;
        @JsonProperty("total_year")
        public int totalYear;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TicketItem {
        @JsonProperty("cert")
        public CertItem certItem;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class CertItem {
            public int enter;
            @JsonProperty("receive_total")
            public int receiveTotal;
            public int total;
        }

        @JsonProperty("event_count")
        public int eventCount;

        @JsonProperty("nearest")
        public NearestItem nearestItem;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class NearestItem {
            @JsonProperty("record_type")
            public String recordType;
            public String name;
            public String type;
        }

        @JsonProperty("nearest_event")
        public NearestEventItem nearestEventItem;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class NearestEventItem {

        }

        @JsonProperty("ticket")
        public Ticket tickets;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Ticket {
            public int check;
            @JsonProperty("receive_total")
            public int receiveTotal;
            public int total;
        }
    }
}
