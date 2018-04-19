package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.CouponCategory;
import com.tgf.kcwc.mvp.model.CouponDistanceFilterModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/24 0024
 * E-mail:hekescott@qq.com
 */

public interface CouponCategoryListView extends WrapView {
    void showCouponList(List<Coupon> couponlist);

    void showRankFilter(ArrayList<DataItem> rankFilterlist);

    void showDistanceOrderFilter(CouponDistanceFilterModel couponDistanceFilterModel);

    void showCategorylist(ArrayList<CouponCategory> categoryList);

    void showAreaList(List<DataItem> areaList);
}
