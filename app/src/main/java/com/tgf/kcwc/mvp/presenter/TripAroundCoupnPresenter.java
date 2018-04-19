package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TripAroundCouponModel;
import com.tgf.kcwc.mvp.model.TripAroundOrgModel;
import com.tgf.kcwc.mvp.model.TripBookService;
import com.tgf.kcwc.mvp.view.TripAroundCouponView;
import com.tgf.kcwc.mvp.view.TripAroundOrgView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/5/16 0016
 * E-mail:hekescott@qq.com
 */

public class TripAroundCoupnPresenter extends WrapPresenter<TripAroundCouponView> {

    private TripAroundCouponView mView;
    private TripBookService mService;

    @Override
    public void attachView(TripAroundCouponView view) {
        mView = view;
        mService = ServiceFactory.getTripBookService();
    }
    public void getAroudCouponlist(int bookLineId,int page, int pageSize){
        RXUtil.execute(mService.getTripAroudCoupon(bookLineId,page,pageSize), new Observer<ResponseMessage<TripAroundCouponModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<TripAroundCouponModel> tripAroundOrgModelResponseMessage) {
                mView.showCouponlist(tripAroundOrgModelResponseMessage.getData().modelList.couponList);
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
}
