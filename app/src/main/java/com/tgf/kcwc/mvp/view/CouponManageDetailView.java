package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.CouponManageDetailModel;

/**
 * Auther: Scott
 * Date: 2017/2/14 0014
 * E-mail:hekescott@qq.com
 */

public interface CouponManageDetailView extends WrapView {
    void showManageViewHead(Coupon coupon);

    void showStatistics(CouponManageDetailModel.CouponStatistics statistics);
}
