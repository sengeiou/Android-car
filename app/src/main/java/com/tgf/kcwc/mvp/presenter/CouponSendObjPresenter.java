package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CouponSendObjModel;
import com.tgf.kcwc.mvp.model.ManageCouponService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CouponSendObjView;
import com.tgf.kcwc.util.RXUtil;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/2/15 0015
 * E-mail:hekescott@qq.com
 */

public class CouponSendObjPresenter extends WrapPresenter<CouponSendObjView> {
    private CouponSendObjView mView;
    private ManageCouponService mService;
    @Override
    public void attachView(CouponSendObjView view) {
            mView = view;
            mService = ServiceFactory.getManageCouponService();
    }

    public  void getSendObjUser(String token,int couponId){
      Subscription subscription = RXUtil.execute(mService.getSendObjRecord(token, couponId), new Observer<ResponseMessage<ArrayList<CouponSendObjModel>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<ArrayList<CouponSendObjModel>> couponEventModelResponseMessage) {
                                mView.showSendObjUser(couponEventModelResponseMessage.getData());
            }
        });

        mSubscriptions.add(subscription);
    }

    public  void reSendCoupon(String token, int listId, int nums, final int pos){
        RXUtil.execute(mService.reSendCoupon(token, listId, nums), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                if(responseMessage.statusCode ==0){
                    mView.showResendSucces(pos);
                }else {
                    mView.errorMessage(responseMessage.statusMessage);
                }
            }
        });
    }
}
