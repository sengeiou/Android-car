package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.AboutModel;
import com.tgf.kcwc.mvp.model.AboutService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.AboutView;
import com.tgf.kcwc.util.RXUtil;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/9/30
 * @describle
 */
public class AboutPresenter extends WrapPresenter<AboutView> {
    Subscription mSubscription;
    AboutView mView;
    AboutService mService;

    @Override
    public void attachView(AboutView view) {
        mView = view;
        mService = ServiceFactory.getAboutService();
    }

    /**
     * @param token
     * @param platform
     */
    public void getAbout(String token,int platform,String place) {
        mSubscription = RXUtil.execute(mService.getAbout(token,platform,place), new Observer<ResponseMessage<ArrayList<AboutModel>>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<ArrayList<AboutModel>> aboutModelResponseMessage) {
                if (aboutModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showAbout(aboutModelResponseMessage.getData().get(0));
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
     * @param token
     * @param platform
     */
    public void getAgreementInfo(String token,int platform) {
        mSubscription = RXUtil.execute(mService.getAgreement(token,platform), new Observer<ResponseMessage<ArrayList<AboutModel>>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<ArrayList<AboutModel>> aboutModelResponseMessage) {
                if (aboutModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showAbout(aboutModelResponseMessage.getData().get(0));
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
