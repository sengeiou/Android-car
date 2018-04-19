package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.CouponDetailModel;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/12 0012
 * E-mail:hekescott@qq.com
 */

public interface CouponDetailView extends WrapView {

    void showHead(CouponDetailModel couponDetailModel);

    void showRule(List<CouponDetailModel.Rrule> rules);

    void showDealers(List<CouponDetailModel.Dealers> dealrs);

    void showDesc(String detail);

}
