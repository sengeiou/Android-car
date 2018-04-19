package com.tgf.kcwc.mvp.presenter;

import android.util.Log;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ExhibitionApplyModel;
import com.tgf.kcwc.mvp.model.ExhibitionApplyService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.ExhibitionApplyView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/9/15
 * @describle
 */
public class ExhibitionApplyPresenter extends WrapPresenter<ExhibitionApplyView> {
    Subscription mSubscription;
    ExhibitionApplyView mView;
    ExhibitionApplyService mService;

    @Override
    public void attachView(ExhibitionApplyView view) {
        mView = view;
        mService = ServiceFactory.getExhibitionApplyService();
    }

    /**
     * 提交申请
     *
     * @param token
     * @param applyId
     * @param applyName
     * @param carImageIn
     * @param carImageOut
     * @param drivingLicense
     * @param idcardBack
     * @param idcardFront
     * @param parkTimeId
     * @param plateNumber
     * @param threadId
     */
    public void commitApply(String token, int applyId, String applyName, int status, String carImageIn, String carImageOut, String drivingLicense,int eventId, String idcardBack, String idcardFront, int parkTimeId, String plateNumber, int threadId,int parkId) {
        mSubscription = RXUtil.execute(mService.commitApply(token, applyId, applyName, status, carImageIn, carImageOut, drivingLicense,eventId, idcardBack, idcardFront, parkTimeId, plateNumber, threadId,parkId), new Observer<ExhibitionApplyModel>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ExhibitionApplyModel exhibitionApplyModelResponseMessage) {
                if (exhibitionApplyModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.commitApplySuccess(exhibitionApplyModelResponseMessage);
                } else {
                    mView.commitApplyFail(exhibitionApplyModelResponseMessage);
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
