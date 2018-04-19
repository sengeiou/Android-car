package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.SetttingService;
import com.tgf.kcwc.mvp.view.FanKuiView;
import com.tgf.kcwc.util.RXUtil;

import retrofit2.http.Field;
import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/7/31 0031
 * E-mail:hekescott@qq.com
 */

public class FankuiPresenter extends WrapPresenter<FanKuiView> {
    private FanKuiView mView;
    private SetttingService mService;

    @Override
    public void attachView(FanKuiView view) {
        this.mView = view;
        mService = ServiceFactory.getSetttingService();
    }

    public void postFanKuiMsg(String token, String tel,
                              String content) {
        RXUtil.execute(mService.postFanKuiMsg(token, tel, content), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.showPostFailed(e.getLocalizedMessage());
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                if (responseMessage.statusCode == 0) {
                    mView.showPostSuccess();
                } else {
                    mView.showPostFailed(responseMessage.statusMessage);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

}
