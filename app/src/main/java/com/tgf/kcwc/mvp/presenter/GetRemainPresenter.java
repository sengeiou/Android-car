package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.GetRemainModel;
import com.tgf.kcwc.mvp.model.GetRemainService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.GetRemainView;
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
public class GetRemainPresenter extends WrapPresenter<GetRemainView> {
    Subscription mSubscription;
    GetRemainView mView;
    GetRemainService mService;
    @Override
    public void attachView(GetRemainView view) {
        mView = view;
        mService = ServiceFactory.getGetRemainService();
    }

    /**
     *
     * @param token
     * @param eventId
     */
    public void getRemain(String token,int eventId){
        mSubscription = RXUtil.execute(mService.getRemain(token, eventId), new Observer<GetRemainModel>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(GetRemainModel responseMessage) {
                if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showGetRemain(responseMessage);
                } else {
                    CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
