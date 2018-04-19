package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BasePageModel;
import com.tgf.kcwc.mvp.model.MyCouponModel;

import java.util.ArrayList;

/**
 * Author：Jenny
 * Date:2017/1/23 10:40
 * E-mail：fishloveqin@gmail.com
 */

public interface CouponView<T> extends WrapView {

    public void showDatas(T t);

    void shwoFailed(String statusMessage);

    void showMyCouponList( ArrayList<MyCouponModel.MyCouponOrder> list);

    void shoMyCouponCount(BasePageModel.Pagination pagination);
}
