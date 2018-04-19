package com.tgf.kcwc.mvp.model;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.presenter.WrapPresenter;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/9/18
 * @describle
 */
public class ExhibitionIntervalPresenter extends WrapPresenter<ExhibitionIntervalView> {
    Subscription mSubscription;
    ExhibitionIntervalView mView;
    ExhibitionIntervalService mService;

    @Override
    public void attachView(ExhibitionIntervalView view) {
        mService = ServiceFactory.getExhibitionIntervalService();
        mView = view;
    }

    /**
     * 展位时段
     *
     * @param token
     * @param eventId
     */
    public void getExhibitionInterval(String token, int eventId) {
        mSubscription = RXUtil.execute(mService.getExhibitionInterval(token, eventId), new Observer<ResponseMessage<ExhibitionIntervalModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<ExhibitionIntervalModel> exhibitionIntervalModelResponseMessage) {
                if (exhibitionIntervalModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showExhibitionInterval(exhibitionIntervalModelResponseMessage.data);
                } else {
                    CommonUtils.showToast(mView.getContext(), exhibitionIntervalModelResponseMessage.statusMessage);
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
