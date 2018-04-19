package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.StoreDetailData;
import com.tgf.kcwc.mvp.model.Topic;

import java.util.List;

/**
 * Author：Jenny
 * Date:2016/12/22 16:38
 * E-mail：fishloveqin@gmail.com
 * 店铺主页
 */

public interface StoreHomeView extends WrapView {

    public void showCouponList(List<Coupon> datas);

    public void showStoreInfo(StoreDetailData data);

    public void showGifts(List<Coupon> datas);

    public void showPrivileges(List<Coupon> datas);
}
