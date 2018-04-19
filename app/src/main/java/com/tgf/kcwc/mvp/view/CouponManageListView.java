package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.CouponManageListModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/14 0014
 * E-mail:hekescott@qq.com
 */

public interface CouponManageListView extends WrapView {
  void   showCouponManageList(ArrayList<CouponManageListModel.CouponManageItem> couponManageItems);
}
