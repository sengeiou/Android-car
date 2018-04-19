package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.PayParamModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.AlipayView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/11/13.
 */

public class AlipayPresenter extends WrapPresenter<AlipayView> {
    AlipayView mView;
    AlipayService mService;
    Subscription mSubscription;

    @Override
    public void attachView(AlipayView view) {
        mView = view;
        mService = ServiceFactory.getAlipayService();
    }

    /**
     * @param token
     * @param orderId
     */
    public void alipay(String token, String orderId) {
        mSubscription = RXUtil.execute(mService.alipay(token, orderId), new Observer<ResponseMessage<String>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<String> payParamModelResponseMessage) {
                if (payParamModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.alipaySuccess(payParamModelResponseMessage.data);
                } else {
                    mView.alipayFail(payParamModelResponseMessage.statusMessage);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
        mSubscriptions.add(mSubscription);
    }
}
