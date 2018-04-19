package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TripBookRecordModel;
import com.tgf.kcwc.mvp.model.TripBookRecordService;
import com.tgf.kcwc.mvp.view.TripBookRecordView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/9/29
 * @describle
 */
public class TripBookRecordPresenter extends WrapPresenter<TripBookRecordView> {
    TripBookRecordService mService;
    TripBookRecordView mView;
    Subscription mSubscription;

    @Override
    public void attachView(TripBookRecordView view) {
        mService = ServiceFactory.getTripBookRecordService();
        mView = view;
    }

    /**
     * 获取路书——我的记录
     * @param rideId
     */
    public void getTripBookRecord(int rideId,int mPageIndex,int mPageSize) {
        mSubscription = RXUtil.execute(mService.getTripBookRecord(rideId, mPageIndex, mPageSize), new Observer<TripBookRecordModel>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(TripBookRecordModel tripBookRecordModel) {
                if (tripBookRecordModel.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showTripBookRecord(tripBookRecordModel.data);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(false);
            }
        });
        mSubscriptions.add(mSubscription);
    }
}
