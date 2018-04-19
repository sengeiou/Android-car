package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CheckSendSeeModel;
import com.tgf.kcwc.mvp.model.CouponEventModel;
import com.tgf.kcwc.mvp.model.CouponService;
import com.tgf.kcwc.mvp.model.ManageCouponService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CouponSendSeeView;
import com.tgf.kcwc.util.RXUtil;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/2/16 0016
 * E-mail:hekescott@qq.com
 */

public class CouponSendPresenter extends WrapPresenter<CouponSendSeeView> {
    private CouponSendSeeView mView;
    private ManageCouponService mService;
    @Override
    public void attachView(CouponSendSeeView view) {
        mView = view;
        mService = ServiceFactory.getManageCouponService();
    }
    public void sendCoupon(String token, int couponId, int distributeId,int timelimit , String users){
      Subscription  subs= RXUtil.execute(mService.sendCoupon(token, couponId, distributeId,timelimit, users), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                if(responseMessage.statusCode == 0){
                    mView.showSendCoupponSucsses();
                }else {
                    mView.errorMessage(responseMessage.statusMessage);
                }
            }
        });
        mSubscriptions.add(subs);
    }

    public void getTicketExhibitInfo(String token, int mTicketId) {
       Subscription subs = RXUtil.execute(mService.getCouponEvent(token, mTicketId), new Observer<ResponseMessage<CouponEventModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<CouponEventModel> couponEventModelResponseMessage) {
                    mView.showSendSeehead(couponEventModelResponseMessage.getData());
            }
        });
        mSubscriptions.add(subs);
    }

    public void checkCouponSeeUser(String token, int distributeId, String mobile) {
      Subscription subs=  RXUtil.execute(mService.checkCouponSeeUser(token, distributeId, mobile), new Observer<ResponseMessage<ArrayList<CheckSendSeeModel>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<ArrayList<CheckSendSeeModel>> arrayListResponseMessage) {
                mView.showCheckCoupon(arrayListResponseMessage.getData());
            }
        });
        mSubscriptions.add(subs);
    }
}
