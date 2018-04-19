package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CouponEventModel;
import com.tgf.kcwc.mvp.model.ManageCouponService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CouponExhibitView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/2/15 0015
 * E-mail:hekescott@qq.com
 */

public class CouponExhibitPresenter extends WrapPresenter<CouponExhibitView> {
    private CouponExhibitView mView;
    private ManageCouponService mService;

    @Override
    public void attachView(CouponExhibitView view) {
        mView = view;
        mService = ServiceFactory.getManageCouponService();
    }

    public void getCouponExhibt(String token, int cpouponId) {
      Subscription subscription = RXUtil.execute(mService.getCouponEvent(token, cpouponId), new Observer<ResponseMessage<CouponEventModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<CouponEventModel> couponEventModelResponseMessage) {
                mView.showCouponExhibit(couponEventModelResponseMessage.getData());
            }
        });
        mSubscriptions.add(subscription);
    }

}
