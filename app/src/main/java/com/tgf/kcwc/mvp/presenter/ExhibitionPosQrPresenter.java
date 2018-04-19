package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CommonEmptyModel;
import com.tgf.kcwc.mvp.model.ExhibitionPosQrModel;
import com.tgf.kcwc.mvp.model.ExhibitionPosQrService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.ExhibitionPosQrView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/10/10
 * @describle
 */
public class ExhibitionPosQrPresenter extends WrapPresenter<ExhibitionPosQrView> {
    ExhibitionPosQrView mView;
    ExhibitionPosQrService mService;
    Subscription mSubscription;

    @Override
    public void attachView(ExhibitionPosQrView view) {
        mView = view;
        mService = ServiceFactory.getExhibitionPosQrService();
    }

    /**
     * 签到
     *
     * @param token
     * @param applyId
     * @param parkCode
     * @param eventId
     */
    public void signIn(String token, int applyId, String parkCode, int eventId) {
        mSubscription = RXUtil.execute(mService.signIn(token, applyId, parkCode, eventId), new Observer<CommonEmptyModel>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(CommonEmptyModel exhibitionPosQrModelResponseMessage) {
                if (exhibitionPosQrModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.exhibitionSuccess(exhibitionPosQrModelResponseMessage.statusMessage);
                } else {
                    mView.exhibitionFail(exhibitionPosQrModelResponseMessage.statusMessage);
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
    /**
     * 打卡
     *
     * @param token
     * @param applyId
     * @param parkCode
     * @param eventId
     */
    public void signOut(String token, int applyId, String parkCode, int eventId) {
        mSubscription = RXUtil.execute(mService.signOut(token, applyId, parkCode, eventId), new Observer<CommonEmptyModel>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(CommonEmptyModel exhibitionPosQrModelResponseMessage) {
                if (exhibitionPosQrModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.exhibitionSuccess(exhibitionPosQrModelResponseMessage.statusMessage);
                } else {
                    mView.exhibitionFail(exhibitionPosQrModelResponseMessage.statusMessage);
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
