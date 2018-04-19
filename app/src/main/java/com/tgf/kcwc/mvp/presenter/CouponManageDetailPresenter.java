package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CouponManageDetailModel;
import com.tgf.kcwc.mvp.model.CouponService;
import com.tgf.kcwc.mvp.model.ManageCouponService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CouponManageDetailView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/2/14 0014
 * E-mail:hekescott@qq.com
 */

public class CouponManageDetailPresenter extends WrapPresenter<CouponManageDetailView> {
    private CouponManageDetailView mView;
    private ManageCouponService mService;

    @Override
    public void attachView(CouponManageDetailView view) {
        mView = view;
        mService = ServiceFactory.getManageCouponService();
    }
    public  void getCouponManageDetail(String token,int couponId){
     Subscription subscription= RXUtil.execute(mService.getCouponManageDetail(token, couponId), new Observer<ResponseMessage<CouponManageDetailModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<CouponManageDetailModel> couponManageDetailModelResponseMessage) {

                CouponManageDetailModel couponManageDetailModel = couponManageDetailModelResponseMessage.getData();
                mView.showManageViewHead(couponManageDetailModel.coupon);
                mView.showStatistics(couponManageDetailModel.statistics);
            }
        });
        mSubscriptions.add(subscription);
    }

}
