package com.tgf.kcwc.mvp.presenter;

import android.util.Log;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CustomizedCollectModel;
import com.tgf.kcwc.mvp.model.CustomizedCollectService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CustomizedCollectView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/8/18
 * @describle
 */
public class CustomizedCollectPresenter extends WrapPresenter<CustomizedCollectView> {
    Subscription mSubscription;
    CustomizedCollectView mView;
    CustomizedCollectService mService;

    @Override
    public void attachView(CustomizedCollectView view) {
        mView = view;
        mService = ServiceFactory.getCustomizedCollectService();
    }

    /**
     * 获取定制及收藏
     *
     * @param token
     * @param friendId
     */
    public void getCustomizedCollect(String token, int friendId) {
        mSubscription = RXUtil.execute(mService.getCustomizedCollect(token, friendId), new Observer<ResponseMessage<CustomizedCollectModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<CustomizedCollectModel> customizedCollectModelResponseMessage) {
                if (customizedCollectModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showCustomized(customizedCollectModelResponseMessage.data.customMadeItem.listItem);
                    mView.showCollect(customizedCollectModelResponseMessage.data.collectItem);
                } else {
                    CommonUtils.showToast(mView.getContext(), customizedCollectModelResponseMessage.statusMessage);
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
