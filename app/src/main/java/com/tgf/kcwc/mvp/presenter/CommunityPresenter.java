package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CommunityModel;
import com.tgf.kcwc.mvp.model.CommunityService;
import com.tgf.kcwc.mvp.model.CommunityView;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/8/17
 * @describle
 */
public class CommunityPresenter extends WrapPresenter<CommunityView> {
    Subscription mSubscription;
    CommunityView mView;
    CommunityService mService;
    @Override
    public void attachView(CommunityView view) {
        mView = view;
        mService = ServiceFactory.getCommunityService();
    }
    public void getCommunity(String token,int friendId,int time){
        mSubscription = RXUtil.execute(mService.getCommunity(token, friendId, time), new Observer<ResponseMessage<CommunityModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<CommunityModel> communityModelResponseMessage) {
                if (communityModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS){
                    mView.showCommunity(communityModelResponseMessage.data);
                }else {
                    CommonUtils.showToast(mView.getContext(),communityModelResponseMessage.statusMessage);
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
