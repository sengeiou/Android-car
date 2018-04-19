package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.CouponOrderDetailModel;
import com.tgf.kcwc.mvp.model.CouponRefoundModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/8/10 0010
 * E-mail:hekescott@qq.com
 */

public interface CouponRefounView extends WrapView {
    void showReundCodes(ArrayList<CouponRefoundModel.RefundCode> canRefundCode);

    void showHead(CouponOrderDetailModel.OrderDetailCoupon coupon);

    void showPostSuccess();

    void showPostFailed(String statusMessage);
}
