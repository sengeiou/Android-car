package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CouponRefoundModel;
import com.tgf.kcwc.mvp.model.CouponService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CouponRefounView;
import com.tgf.kcwc.util.RXUtil;

import retrofit2.http.Field;
import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/8/10 0010
 * E-mail:hekescott@qq.com
 */

public class CouponRefounPresenter extends WrapPresenter<CouponRefounView> {
    private CouponRefounView mView;
    private CouponService mService;

    @Override
    public void attachView(CouponRefounView view) {
        mView = view;
        mService = ServiceFactory.getCouponService();
    }
    public void getRefoundInfo(int orderId,String token){

        RXUtil.execute(mService.getRefondInfo(token, orderId), new Observer<ResponseMessage<CouponRefoundModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<CouponRefoundModel> couponRefoundModelResponseMessage) {
                CouponRefoundModel couponRefoundModel = couponRefoundModelResponseMessage.getData();

                mView.showReundCodes(couponRefoundModel.canRefundCode);
                mView.showHead(couponRefoundModel.coupon);
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void postRefond(String token, String codeIds, int type,String why, String backNote){
        RXUtil.execute(mService.postRefond(token, codeIds, type, why, backNote), new Observer<ResponseMessage>() {
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
                if (responseMessage.statusCode == 0) {
                    mView.showPostSuccess();
                } else {
                    mView.showPostFailed(responseMessage.statusMessage);
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
