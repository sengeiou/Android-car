package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.GetApplyModel;
import com.tgf.kcwc.mvp.model.GetApplyService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.GetApplyView;
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
public class GetApplyPresenter extends WrapPresenter<GetApplyView> {
    Subscription mSubscription;
    GetApplyView mView;
    GetApplyService mService;

    @Override
    public void attachView(GetApplyView view) {
        mView = view;
        mService = ServiceFactory.getGetApplyService();
    }

    /**
     * 二手车参展申请
     * @param token
     * @param applyId
     */
    public void getApply(String token, int applyId) {
        mSubscription = RXUtil.execute(mService.getApply(token, applyId), new Observer<ResponseMessage<GetApplyModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<GetApplyModel> getApplyModelResponseMessage) {
                if (getApplyModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showGetApply(getApplyModelResponseMessage.data);
                } else {
                    CommonUtils.showToast(mView.getContext(), getApplyModelResponseMessage.statusMessage);
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
