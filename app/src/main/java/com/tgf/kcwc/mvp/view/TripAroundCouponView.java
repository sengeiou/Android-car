package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.TripBookDetailModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/16 0016
 * E-mail:hekescott@qq.com
 */

public interface TripAroundCouponView extends WrapView {
    void showCouponlist(ArrayList<TripBookDetailModel.Coupon> couponList);
}
