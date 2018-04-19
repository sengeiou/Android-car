package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.PayParamModel;
import com.tgf.kcwc.mvp.model.PayParamService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.PayParamView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/9/21
 * @describle
 */
public class PayParamPresenter extends WrapPresenter<PayParamView> {
    Subscription mSubscription;
    PayParamView mView;
    PayParamService mService;

    @Override
    public void attachView(PayParamView view) {
        mView = view;
        mService = ServiceFactory.getPayParamService();
    }

    /**
     * 上传支付参数
     *
     * @param token
     */
    public void postPayParam(String token, int orderId,String tradeType) {
        mSubscription = RXUtil.execute(mService.getPayParam(token, orderId,tradeType), new Observer<PayParamModel>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(PayParamModel payParamModel) {
                if (payParamModel.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.payParamSuccess(payParamModel.data);
                } else {
//                    CommonUtils.showToast(mView.getContext(), payParamModel.statusMessage);
                    mView.payParamFail(payParamModel);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(false);
            }
        });
        mSubscriptions.add(mSubscription);
    }
}
