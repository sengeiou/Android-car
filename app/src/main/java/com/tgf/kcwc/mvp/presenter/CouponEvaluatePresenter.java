package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CouponOrderDetailModel;
import com.tgf.kcwc.mvp.model.CouponService;
import com.tgf.kcwc.mvp.model.MyCouponModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CouponEvaluateView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/10/12 0012
 * E-mail:hekescott@qq.com
 */

public class CouponEvaluatePresenter extends WrapPresenter<CouponEvaluateView> {

    private CouponEvaluateView mView;
    private CouponService mService;

    @Override
    public void attachView(CouponEvaluateView view) {
        mView = view;
        mService = ServiceFactory.getCouponService();

    }

    public void getCouponHeaddetail(String token, int orderId) {
        RXUtil.execute(mService.getCouponOderDetal(token, orderId, null, null), new Observer<ResponseMessage<CouponOrderDetailModel>>() {
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
                if (couponOrderDetailModelResponseMessage.statusCode == 0) {
                    CouponOrderDetailModel couponOrderDetailModel = couponOrderDetailModelResponseMessage.getData();
                    CouponOrderDetailModel.OrderDetailCoupon orderDetailCoupon = couponOrderDetailModel.coupon;
                    mView.showHead(orderDetailCoupon);
                } else {
                    CommonUtils.showToast(mView.getContext(), couponOrderDetailModelResponseMessage.statusMessage);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void postCouponEvalue(String token, int starService,int star,int starShop,int invisible,int resourceId,String module,String text, String imgs) {
       Subscription subscription =   RXUtil.execute(mService.postCouponEvalue(token, starService, star, invisible, starShop, resourceId, module, text, imgs), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                if(responseMessage.statusCode==0){
                    mView.showPostSuccess();
                }else {
                    mView.showPostFailed(responseMessage.statusMessage);
                }

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
