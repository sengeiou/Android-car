package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CouponOrderDetailModel;
import com.tgf.kcwc.mvp.model.CouponService;
import com.tgf.kcwc.mvp.model.MyCouponModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CouponOrderDetailView;
import com.tgf.kcwc.util.RXUtil;

import java.util.ArrayList;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/8/8 0008
 * E-mail:hekescott@qq.com
 */

public class CouponOrderDetailPresenter extends WrapPresenter<CouponOrderDetailView> {
    private CouponOrderDetailView mView;
    private CouponService mService;

    @Override
    public void attachView(CouponOrderDetailView view) {
        mView = view;
        mService = ServiceFactory.getCouponService();
    }
    public void getCouponOrderdetail(String token,int orderId,String lat,String lng){
        RXUtil.execute(mService.getCouponOderDetal(token, orderId,lat,lng), new Observer<ResponseMessage<CouponOrderDetailModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<CouponOrderDetailModel> couponOrderDetailModelResponseMessage) {
                CouponOrderDetailModel couponOrderDetailModel    = couponOrderDetailModelResponseMessage.getData();
                ArrayList<MyCouponModel.CouponCode> codes  =  couponOrderDetailModel.codes;
               CouponOrderDetailModel.OrderDetailCoupon orderDetailCoupon =  couponOrderDetailModel.coupon;
                mView.showCodeList(codes);
                mView.showHeads(orderDetailCoupon);
                mView.showDealer(couponOrderDetailModel.dealers);
                mView.showOrderInfo(couponOrderDetailModel);
                mView.showOnlineList(couponOrderDetailModel.online);

            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
}
