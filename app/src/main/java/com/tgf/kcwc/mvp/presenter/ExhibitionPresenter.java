package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.SelectExhibitionModel;
import com.tgf.kcwc.mvp.model.SelectExhibitionService;
import com.tgf.kcwc.mvp.view.ExhibitionView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.concurrent.ExecutorService;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/9/13
 * @describle
 */
public class ExhibitionPresenter extends WrapPresenter<ExhibitionView> {
    ExhibitionView mView;
    Subscription mSubscription;
    SelectExhibitionService mService;

    @Override
    public void attachView(ExhibitionView view) {
        mView = view;
        mService = ServiceFactory.getSelectExhibitionService();
    }

    /**
     * 获取展会列表
     * @param token
     * @param cityId
     */
    public void getSelectExhibition(String token, int cityId) {
        mSubscription = RXUtil.execute(mService.getSelectExhibition(token, cityId), new Observer<SelectExhibitionModel>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(SelectExhibitionModel selectExhibitionModelResponseMessage) {
                if (selectExhibitionModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showExhibitionList(selectExhibitionModelResponseMessage);
                } else {
                    CommonUtils.showToast(mView.getContext(), selectExhibitionModelResponseMessage.statusMessage);
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
