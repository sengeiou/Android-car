package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.CouponSendRecordModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/15 0015
 * E-mail:hekescott@qq.com
 */

public interface CouponSendRecordView extends WrapView {
    void showCouponSendRecord(ArrayList<CouponSendRecordModel> couponSendRecordModels);
}
