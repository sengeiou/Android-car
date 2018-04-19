package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.CouponDetailModel;
import com.tgf.kcwc.mvp.model.CouponOrderDetailModel;
import com.tgf.kcwc.mvp.model.MyCouponModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/8/8 0008
 * E-mail:hekescott@qq.com
 */

public interface CouponOrderDetailView extends WrapView{
    void showCodeList(ArrayList<MyCouponModel.CouponCode> codes);

    void showHeads(CouponOrderDetailModel.OrderDetailCoupon orderDetailCoupon);

    void showDealer(ArrayList<CouponDetailModel.Dealers> dealers);

    void showOrderInfo(CouponOrderDetailModel couponOrderDetailModel);

    void showOnlineList(ArrayList<CouponDetailModel.OnlineItem> online);
}
