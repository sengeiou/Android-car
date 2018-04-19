package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.GeneralizationService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.UpdateModel;
import com.tgf.kcwc.mvp.view.UpdateView;
import com.tgf.kcwc.util.RXUtil;
import com.tgf.kcwc.util.SharedPreferenceUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/7/11 0011
 * E-mail:hekescott@qq.com
 */

public class StartPagePresenter extends WrapPresenter<UpdateView> {

    private GeneralizationService mService;
    private UpdateView mView;

    @Override
    public void attachView(UpdateView view) {
        mView = view;
        mService = ServiceFactory.getGeneralizationService();
    }
    public void getUpdateApi(String verCode){
        Subscription subscription = RXUtil.execute(mService.getUpdateApi(verCode), new Observer<ResponseMessage<UpdateModel>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ResponseMessage<UpdateModel> updateModelResponseMessage) {
                if (updateModelResponseMessage.getData() == null) {
                    mView.showNoUpdate();
                } else {
                    mView.showUpdate(updateModelResponseMessage.getData());

                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
