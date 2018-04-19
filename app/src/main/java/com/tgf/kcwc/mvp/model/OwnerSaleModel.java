package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @anthor noti
 * @time 2017/9/19
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OwnerSaleModel {
    @JsonProperty("code")
    public int statusCode;
    @JsonProperty("msg")
    public String statusMessage;

    @JsonProperty("data")
    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("event_config")
        public EventConfig eventConfig;
        @JsonProperty("thread_id")
        public int threadId;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class EventConfig {
            public String inputs;
            public int id;
            @JsonProperty("event_id")
            public int eventId;
            @JsonProperty("apply_start_time")
            public String applyStartTime;
            @JsonProperty("apply_end_time")
            public String applyEndTime;
            @JsonProperty("examine_phone")
            public String examinePhone;
            @JsonProperty("examine_user_ids")
            public String examineUserIds;
            @JsonProperty("apply_sale_note")
            public String applySaleNote;
            @JsonProperty("addtime")
            public String addtime;
            @JsonProperty("join_welfare")
            public String joinWelfare;
            @JsonProperty("buy_welfare")
            public String buyWelfare;
            @JsonProperty("buy_welfare_title")
            public String buyWelfareTitle;
            @JsonProperty("examine_mobile")
            public String examineMobile;
            @JsonProperty("buy_know")
            public String buyKnow;
            @JsonProperty("buy_cost")
            public String buyCost;
            @JsonProperty("apply_max_user")
            public int applyMaxUser;
            @JsonProperty("create_user_id")
            public int createUserId;
            @JsonProperty("include_today")
            public int includeToday;
            public int delete;
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
            @JsonProperty("orvertime_no_in")
            public int orvertimeNoIn;
            @JsonProperty("advance_out_time")
            public int advanceOutTime;
            @JsonProperty("out_last_time")
            public int outLastTime;
            @JsonProperty("overtime_no_out")
            public int overtimeNoOut;
            @JsonProperty("advance_cancel_order")
            public int advanceCancelOrder;
            @JsonProperty("examine_time")
            public int examineTime;
            @JsonProperty("cancel_deposit_check")
            public int cancelDepositCheck;
        }
    }

}
