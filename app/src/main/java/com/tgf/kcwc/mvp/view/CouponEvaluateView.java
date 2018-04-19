package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.CouponOrderDetailModel;

/**
 * Auther: Scott
 * Date: 2017/8/14 0014
 * E-mail:hekescott@qq.com
 */

public interface CouponEvaluateView extends WrapView {
    void showHead(CouponOrderDetailModel.OrderDetailCoupon orderDetailCoupon);

    void showPostFailed(String statusMessage);

    void showPostSuccess();
}
