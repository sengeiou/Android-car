package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ThirdPartyService;
import com.tgf.kcwc.mvp.view.ThirdPartyView;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/11/14.
 */

public class ThirdPartyPresenter extends WrapPresenter<ThirdPartyView> {
    ThirdPartyService mService;
    ThirdPartyView mView;
    Subscription mSubscription;

    @Override
    public void attachView(ThirdPartyView view) {
        mView = view;
        mService = ServiceFactory.getThirdPartyService();
    }


    public void getWXAuthToken(Map<String, String> params) {
        mSubscription = RXUtil.execute(mService.getWXAuthToken(params), new Observer<ResponseBody>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseBody responseMessage) {
                mView.showData(responseMessage);
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
}
