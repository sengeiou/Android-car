package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/8/11 0011
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RefondDetailModel {

    public int id;
    public String codes;
    public String refund_type;
    public String refund_success_time;
    public String refund_money;
    public int check_status;
    public CouponOrderDetailModel.OrderDetailCoupon coupon;
    public ArrayList<RefondProgress> progress_list;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RefondProgress{
        @JsonProperty("do")
        public String action;
        public String time;
    }
}
