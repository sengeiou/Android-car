package com.tgf.kcwc.mvp.view;

import java.util.List;

import com.tgf.kcwc.mvp.model.DiscountCouponModel;

/**
 * Auther: Scott
 * Date: 2017/4/25 0025
 * E-mail:hekescott@qq.com
 */

public interface DiscountCouponView extends WrapView {
    void showLimitList(List<DiscountCouponModel.DiscountCouponItem> limitModellist);
}
