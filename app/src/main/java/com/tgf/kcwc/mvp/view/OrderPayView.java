package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.CouponPayModel;
import com.tgf.kcwc.mvp.model.OrderModel;

/**
 * Author：Jenny
 * Date:2017/1/19 18:38
 * E-mail：fishloveqin@gmail.com
 */

public interface OrderPayView<T> extends WrapView {

    void showData(T t);

    void showPayCouponResult(CouponPayModel data);
}
