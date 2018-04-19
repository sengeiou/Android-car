package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/8/8 0008
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponOrderDetailModel {

    public int id;
    @JsonProperty("order_sn")
    public String orderSn;
    @JsonProperty("total_price")
    public String totalPrice;
    @JsonProperty("coupon_total")
    public int couponTotal;
    @JsonProperty("is_pay")
    public int isPay;
    @JsonProperty("pay_time")
    public String payTime;
    @JsonProperty("refund_money")
    public String refundMoney;
    @JsonProperty("create_time")
    public String createTime;
    @JsonProperty("can_evaluate")
    public int canEvaluate;
    @JsonProperty("can_refund")
    public int canRefund;
    public User user;
    public OrderDetailCoupon coupon;
    public ArrayList<MyCouponModel.CouponCode> codes;
    public ArrayList<CouponDetailModel.Dealers> dealers;
    public ArrayList<CouponDetailModel.OnlineItem> online;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderDetailCoupon{
        public int id;
        public String cover;
        public int refund_type;
        public String desc;
        public String title;
        public String begin_time;
        public String end_time;
        public String price;
        public int send_type;
        public String denomination;
        public int is_expire;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User{

        public int id;
        public String nickname;
        public String tel;
        public String avatar;
    }

}
