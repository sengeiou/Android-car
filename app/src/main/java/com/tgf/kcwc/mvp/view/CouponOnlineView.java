package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BasePageModel;
import com.tgf.kcwc.mvp.model.MyCouponModel;
import com.tgf.kcwc.mvp.model.OnlineCoupon;

import java.util.ArrayList;

/**
 * Author：Jenny
 * Date:2017/1/23 10:40
 * E-mail：fishloveqin@gmail.com
 */

public interface CouponOnlineView extends WrapView {


    void showSuccess(ArrayList<OnlineCoupon.OnlineCouponItem> onlineCouponList);
}
