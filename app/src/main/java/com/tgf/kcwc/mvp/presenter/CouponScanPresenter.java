package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ManageCouponService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CouponScanView;
import com.tgf.kcwc.util.RXUtil;

import retrofit2.http.Field;
import rx.Observer;
import rx.functions.Action0;

/**
 * Created by heke on 17/8/20.
 */

public class CouponScanPresenter extends WrapPresenter<CouponScanView> {
    CouponScanView mView;
    ManageCouponService manageCouponService;
    @Override
    public void attachView(CouponScanView view) {

        mView = view;

        manageCouponService = ServiceFactory.getManageCouponService();
    }
    public void postGetSendCoupon(String token, String couponId, String distributeId,String num){
        RXUtil.execute(manageCouponService.postScanSend(token, couponId, distributeId, num), new Observer<ResponseMessage>() {
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
                    mView.showScanSuccess();
                }else{
                    mView.showScanFailed();
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
}
