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
public class OrderPayModel {
    @JsonProperty("ma")
    public Ma maList;
    @JsonProperty("sla")
    public Sla slaList;
    @JsonProperty("config")
    public Config config;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Ma {
        public int id;
        @JsonProperty("order_sn")
        public String orderSn;
        public String price;
        public int quantity;
        public String total;
        public String model;
        public String title;
        public int burse;
        public int coupon;
        @JsonProperty("coupon_id")
        public int couponId;
        @JsonProperty("other_pay")
        public int otherPay;
        @JsonProperty("other_pay_method")
        public int otherPayMethod;
        public int status;
        @JsonProperty("refund_status")
        public int refundStatus;
        @JsonProperty("user_id")
        public int userId;
        public String addtime;
        @JsonProperty("pay_time")
        public String payTime;
        public int discount;
        @JsonProperty("model_id")
        public int modelId;
        @JsonProperty("refund_time")
        public String refundTime;
        @JsonProperty("apply_refund_time")
        public String applyRefundTime;
        public int source;
        public String channel;
        public int delete;
        public String phone;
        @JsonProperty("allow_pay_method")
        public int allowPayMethod;
        @JsonProperty("user_name")
        public String userName;
        @JsonProperty("pay_back_sn")
        public String payBackSn;
        @JsonProperty("pay_openid")
        public String payOpenid;
        @JsonProperty("other_pay_amount")
        public int otherPayAmount;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sla {
        public int id;
        @JsonProperty("main_order_id")
        public int mainOrderId;
        @JsonProperty("apply_id")
        public int applyId;
        @JsonProperty("park_time_id")
        public int parkTimeId;
        @JsonProperty("event_id")
        public int eventId;
        @JsonProperty("service_charge")
        public String serviceCharge;
        @JsonProperty("deposit")
        public String deposit;
        @JsonProperty("total")
        public String total;
        public String addtime;
        public int type;
        public int delete;
        @JsonProperty("park_name")
        public String parkName;
        @JsonProperty("event_booth_name")
        public String eventBoothName;
        @JsonProperty("start_time")
        public String startTime;
        @JsonProperty("end_time")
        public String endTime;
        @JsonProperty("time_solt")
        public String timeSolt;
        @JsonProperty("thread_id")
        public int threadId;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Config {
        @JsonProperty("examine_time")
        public int examineTime;
        @JsonProperty("examine_phone")
        public String examinePhone;
        @JsonProperty("examine_mobile")
        public String examineMobile;
    }
}
