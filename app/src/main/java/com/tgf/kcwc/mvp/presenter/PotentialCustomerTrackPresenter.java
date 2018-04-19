package com.tgf.kcwc.mvp.presenter;

import android.util.Log;

import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CustomerTrackModel;
import com.tgf.kcwc.mvp.model.PotentialCustomerTrackService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.PotentialCustomerTrackView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/7/31 11:01
 */

public class PotentialCustomerTrackPresenter extends WrapPresenter<PotentialCustomerTrackView> {
    PotentialCustomerTrackView mView;
    PotentialCustomerTrackService mService;

    @Override
    public void attachView(PotentialCustomerTrackView view) {
        mView = view;
        mService = ServiceFactory.getTrackService();
    }

    /**
     * 获取潜客跟踪列表
     * @param token
     */
    public void getTrackList(String token,int page) {
        RXUtil.execute(mService.getTrackList(token,page), new Observer<ResponseMessage<CustomerTrackModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<CustomerTrackModel> customerTrackModelResponseMessage) {
                mView.showTrackList(customerTrackModelResponseMessage.data.list);
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
}
