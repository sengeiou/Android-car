package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CouponService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.ScanOffDetailModel;
import com.tgf.kcwc.mvp.view.ScanOffCouponDetailView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/5/25 0025
 * E-mail:hekescott@qq.com
 */

public class ScanOffCouponDetailPresenter extends WrapPresenter<ScanOffCouponDetailView> {

    private ScanOffCouponDetailView mView;
    private CouponService mService;

    @Override
    public void attachView(ScanOffCouponDetailView view) {
        mView = view;
        mService = ServiceFactory.getCouponService();
    }

    public void getScanOffDetail(String token,String couponId) {
       Subscription subscription =   RXUtil.execute(mService.getScanoffDetail(token,couponId), new Observer<ResponseMessage<ScanOffDetailModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);

            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<ScanOffDetailModel> scanOffDetailModelResponseMessage) {
                mView.showScanOffList(scanOffDetailModelResponseMessage.getData().list);
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
