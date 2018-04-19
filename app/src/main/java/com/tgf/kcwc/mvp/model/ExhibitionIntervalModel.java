package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/9/18
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExhibitionIntervalModel {
    public int id;
    public String name;
    @JsonProperty("start_time")
    public String startTime;
    @JsonProperty("end_time")
    public String endTime;
    public String cover;
    public String address;
    @JsonProperty("time_slots")
    public ArrayList<TimeSlots> timeSlotses;
    @JsonProperty("booths")
    public ArrayList<Booths> boothses;
    @JsonProperty("halls")
    public ArrayList<Halls> hallses;
    @JsonProperty("helpList")
    public ArrayList<HelpList> helpList;
    @JsonProperty("config")
    public Config config;

    @JsonProperty("event_now_status")
    public String eventNowStatus;

    public static class HelpList {
        public String content;
        public String title;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TimeSlots {
        public boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public int id;
        @JsonProperty("park_time")
        public String parkTime;
        @JsonProperty("time_format")
        public String timeFormat;

        public String getTimeFormat() {
            return timeFormat;
        }

        public void setTimeFormat(String timeFormat) {
            this.timeFormat = timeFormat;
        }

        @JsonProperty("slots")
        public ArrayList<Slots> slotses;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Slots {
            public boolean isSelect;

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }

            public int id;
            public int delete;
            @JsonProperty("start_time")
            public String startTime;
            @JsonProperty("end_time")
            public String endTime;
            @JsonProperty("time_format")
            public String timeFormat;
            @JsonProperty("is_full")
            public int isFull;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Booths {
        public boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public int id;
        @JsonProperty("hall_id")
        public int hallId;
        public String name;
        public String cover;
        @JsonProperty("total_park")
        public int totalPark;
        @JsonProperty("not_used_park")
        public int notUsedPark;
        @JsonProperty("is_full")
        public int isFull;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Halls {
        public int id;
        @JsonProperty("event_id")
        public int eventId;
        public String name;
        @JsonProperty("map_pic")
        public String mapPic;
        public String description;
        @JsonProperty("create_by")
        public int createBy;
        @JsonProperty("create_time")
        public String createTime;
        @JsonProperty("update_by")
        public int updateBy;
        @JsonProperty("update_time")
        public String updateTime;
        public int status;
        public int sort;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Config {
        public int id;
        @JsonProperty("apply_max_user")
        public int applyMaxUser;
        @JsonProperty("edit_max_user")
        public int editMaxUser;
        @JsonProperty("examine_max_apply")
        public int examineMaxApply;
        @JsonProperty("advance_in_time")
        public int advanceInTime;
        @JsonProperty("overtime_time")
        public int overtimeTime;
        @JsonProperty("close_order_type")
        public int closeOrderType;
        @JsonProperty("examine_time")
        public int examineTime;
        @JsonProperty("advance_cancel_order")
        public int advanceCancelOrder;
        @JsonProperty("include_today")
        public int includeToday;
        @JsonProperty("examine_phone")
        public String examinePhone;
        @JsonProperty("apply_end_time")
        public String applyEndTime;
        @JsonProperty("apply_sale_note")
        public String applySaleNote;
        @JsonProperty("join_welfare")
        public String joinWelfare;
        public String inputs;
    }
}
