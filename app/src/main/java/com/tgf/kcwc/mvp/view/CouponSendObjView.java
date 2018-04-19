package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.CouponSendObjModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/15 0015
 * E-mail:hekescott@qq.com
 */

public interface CouponSendObjView extends WrapView {
    void showSendObjUser(ArrayList<CouponSendObjModel> userlist);
    void showResendSucces(int pos);

    void errorMessage(String statusMessage);
}
