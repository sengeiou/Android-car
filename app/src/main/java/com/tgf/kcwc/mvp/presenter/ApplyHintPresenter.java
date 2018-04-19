package com.tgf.kcwc.mvp.presenter;

import com.amap.api.location.APSService;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ApplyHintModel;
import com.tgf.kcwc.mvp.model.ApplyHintService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.ApplyHintView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/11/6
 * @describle
 */
public class ApplyHintPresenter extends WrapPresenter<ApplyHintView> {
    Subscription mSubscription;
    ApplyHintView mView;
    ApplyHintService mService;

    @Override
    public void attachView(ApplyHintView view) {
        mView = view;
        mService = ServiceFactory.getApplyHintService();
    }

    public void getApplyHint(String token, int eventId, int type) {
        mSubscription = RXUtil.execute(mService.getApplyHint(token, eventId, type), new Observer<ResponseMessage<ApplyHintModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<ApplyHintModel> applyHintModelResponseMessage) {
                if (applyHintModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showApplyHint(applyHintModelResponseMessage.data);
                } else {
                    CommonUtils.showToast(mView.getContext(), applyHintModelResponseMessage.statusMessage);
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
