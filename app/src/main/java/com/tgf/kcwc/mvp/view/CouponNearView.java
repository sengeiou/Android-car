package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.CouponCategory;
import com.tgf.kcwc.mvp.model.CouponDistanceFilterModel;
import com.tgf.kcwc.mvp.model.CouponNearModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by susu on 17/1/22.
 */

public interface CouponNearView extends WrapView {

    void showNearList(CouponNearModel couponNearModel);
    void showRankFilter(ArrayList<DataItem> rankFilterlist);

    void showDistanceOrderFilter(CouponDistanceFilterModel couponDistanceFilterModel);

    void showCategorylist(ArrayList<CouponCategory> categoryList);

//    void showAreaList(List<DataItem> areaList);
}
