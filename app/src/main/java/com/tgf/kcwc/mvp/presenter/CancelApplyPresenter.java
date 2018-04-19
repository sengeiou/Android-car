package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CancelApplyService;
import com.tgf.kcwc.mvp.model.CommonEmptyModel;
import com.tgf.kcwc.mvp.view.CancelApplyView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/11/16.
 */

public class CancelApplyPresenter extends WrapPresenter<CancelApplyView> {
    CancelApplyView mView;
    CancelApplyService mService;
    Subscription mSubscription;

    @Override
    public void attachView(CancelApplyView view) {
        mView = view;
        mService = ServiceFactory.getCancelApplyService();
    }

    /**
     * 取消参展
     * @param token
     * @param applyId
     */
    public void cancelApply(String token, int applyId) {
        mSubscription = RXUtil.execute(mService.cancelApply(token, applyId), new Observer<CommonEmptyModel>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(CommonEmptyModel commonEmptyModel) {
                if (commonEmptyModel.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.cancelSuccess(commonEmptyModel.statusMessage);
                } else {
                    mView.cancelFail(commonEmptyModel.statusMessage);
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
