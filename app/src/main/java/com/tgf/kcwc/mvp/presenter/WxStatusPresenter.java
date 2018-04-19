package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.WxStatusModel;
import com.tgf.kcwc.mvp.model.WxStatusService;
import com.tgf.kcwc.mvp.view.WxStatusView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/11/14.
 */

public class WxStatusPresenter extends WrapPresenter<WxStatusView> {
    WxStatusService mService;
    WxStatusView mView;
    Subscription mSubscription;

    @Override
    public void attachView(WxStatusView view) {
        mView = view;
        mService = ServiceFactory.getWxStatusService();
    }

    /**
     * 获取微信支付的状态
     * @param token
     * @param orderId
     */
    public void getWxStatus(String token, String orderId) {
        mSubscription = RXUtil.execute(mService.getWxStatus(token, orderId), new Observer<ResponseMessage<String>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<String> wxStatusModelResponseMessage) {
                if (wxStatusModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.wxStatusSuccess(wxStatusModelResponseMessage.data);
                } else {
                    mView.wxStatusFail(wxStatusModelResponseMessage.data);
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
