package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.CouponCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/10 0010
 * E-mail:hekescott@qq.com
 */

public interface CouponRecomentView extends WrapView {

       void showRecomendHead(Coupon heaCoupon);
       void showRecomendList(ArrayList<Coupon> couponList);
       void showCategorylist(ArrayList<CouponCategory> couponCategoryList);

    void showBannerView(List<BannerModel.Data> bannerList);
}
