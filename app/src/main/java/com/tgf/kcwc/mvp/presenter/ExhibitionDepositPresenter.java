package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ExhibitionDepositService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.ExhibitDetailView;
import com.tgf.kcwc.mvp.view.ExhibitionDepositView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/9/14
 * @describle
 */
public class ExhibitionDepositPresenter extends WrapPresenter<ExhibitionDepositView> {
    Subscription mSubscription;
    ExhibitionDepositView mView;
    ExhibitionDepositService mService;

    @Override
    public void attachView(ExhibitionDepositView view) {
        mView = view;
        mService = ServiceFactory.getExhibitionDepositService();
    }

    /**
     * 展位投诉
     * @param token
     * @param applyId
     * @param complain
     */
    public void exhibitionDeposit(String token, int applyId, String complain,String complainImg1, String complainImg2, String complainImg3, String complainImg4, String complainImg5) {
        mSubscription = RXUtil.execute(mService.exhibitionDeposit(token, applyId, complain,complainImg1, complainImg2, complainImg3, complainImg4, complainImg5), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.depositSuccess(responseMessage);
                } else {
                    mView.depositFail(responseMessage);
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
