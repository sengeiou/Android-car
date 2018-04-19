package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.CouponManageDetailModel;
import com.tgf.kcwc.mvp.model.ManageCouponService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.SendCodeCouponView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/8/17 0017
 * E-mail:hekescott@qq.com
 */

public class SendCodeCouponPresenter extends WrapPresenter<SendCodeCouponView> {

    private ManageCouponService mService;
    private SendCodeCouponView mView;

    @Override
    public void attachView(SendCodeCouponView view) {
        mView = view;
        mService = ServiceFactory.getManageCouponService();
    }
    public  void getCouponManageDetail(String token,int couponId){
        Subscription subscription= RXUtil.execute(mService.getCouponManageDetail(token, couponId), new Observer<ResponseMessage<CouponManageDetailModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<CouponManageDetailModel> couponManageDetailModelResponseMessage) {
                CouponManageDetailModel couponManageDetailModel = couponManageDetailModelResponseMessage.getData();
                mView.showManageViewHead(couponManageDetailModel);
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
        mSubscriptions.add(subscription);
    }
}
