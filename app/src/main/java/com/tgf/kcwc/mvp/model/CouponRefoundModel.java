package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/8/10 0010
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponRefoundModel {

    public CouponOrderDetailModel.OrderDetailCoupon coupon;
    public ArrayList<RefundCode> canRefundCode;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RefundCode {

        public int id;
        public String code;
        public String price;
        public boolean isSelected;
    }
}
