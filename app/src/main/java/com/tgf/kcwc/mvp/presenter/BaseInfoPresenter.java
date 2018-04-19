package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseInfoModel;
import com.tgf.kcwc.mvp.model.BaseInfoService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.BaseInfoView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/8/16
 * @describle
 */
public class BaseInfoPresenter extends WrapPresenter<BaseInfoView> {
    Subscription mSubscription;
    BaseInfoView mView;
    BaseInfoService mService;

    @Override
    public void attachView(BaseInfoView view) {
        mView = view;
        mService = ServiceFactory.getBaseInfoService();
    }

    /**
     * 获取基本信息
     */
    public void getBaseInfo(String token, int friendId) {
        mSubscription = RXUtil.execute(mService.getBaseInfo(token, friendId), new Observer<ResponseMessage<BaseInfoModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<BaseInfoModel> baseInfoModelResponseMessage) {
                if (baseInfoModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showBaseInfo(baseInfoModelResponseMessage.data);
                    mView.showAuth(baseInfoModelResponseMessage.data.authItem);
                } else {
                    CommonUtils.showToast(mView.getContext(), baseInfoModelResponseMessage.statusMessage);
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
