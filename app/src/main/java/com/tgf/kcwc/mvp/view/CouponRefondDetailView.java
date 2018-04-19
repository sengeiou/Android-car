package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.CouponOrderDetailModel;
import com.tgf.kcwc.mvp.model.RefondDetailModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/8/11 0011
 * E-mail:hekescott@qq.com
 */

public interface CouponRefondDetailView extends WrapView {
    void showHead(CouponOrderDetailModel.OrderDetailCoupon coupon);

    void showRefondInfo(RefondDetailModel refondDetailModel);

    void showRefondProgress(ArrayList<RefondDetailModel.RefondProgress> progressList);
}
