package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.CheckSendSeeModel;
import com.tgf.kcwc.mvp.model.CouponEventModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/16 0016
 * E-mail:hekescott@qq.com
 */

public interface CouponSendSeeView extends WrapView {
    void showSendCoupponSucsses();
   void showSendSeehead(CouponEventModel event);
    void showCheckCoupon(ArrayList<CheckSendSeeModel> checkSendSeeModelArrayList);

    void errorMessage(String statusMessage);
}
