package com.tgf.kcwc.mvp.presenter;

import android.util.Log;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.SelectExhibitionPosModel;
import com.tgf.kcwc.mvp.model.SelectExhibitionPosService;
import com.tgf.kcwc.mvp.view.SelectExhibitionPosView;
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
public class SelectExhibitionPosPresenter extends WrapPresenter<SelectExhibitionPosView> {
    Subscription mSubscription;
    SelectExhibitionPosService mService;
    SelectExhibitionPosView mView;

    @Override
    public void attachView(SelectExhibitionPosView view) {
        mView = view;
        mService = ServiceFactory.getSelectExhibitionPosService();
    }

    /**
     * 获取展位相关
     * @param token
     * @param timeSlotId
     */
    public void getSelectExhibitionPos(String token, int boothId,int timeSlotId) {
        mSubscription = RXUtil.execute(mService.getSelectExhibitionPos(token,boothId,timeSlotId), new Observer<SelectExhibitionPosModel>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(SelectExhibitionPosModel selectExhibitionPosModel) {
                if (selectExhibitionPosModel.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showExhibitionPos(selectExhibitionPosModel);
                } else {
                    CommonUtils.showToast(mView.getContext(), selectExhibitionPosModel.statusMessage);
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
