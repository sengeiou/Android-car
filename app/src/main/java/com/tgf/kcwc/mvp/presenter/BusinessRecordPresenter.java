package com.tgf.kcwc.mvp.presenter;

import android.util.Log;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BusinessRecordModel;
import com.tgf.kcwc.mvp.model.BusinessRecordService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.BusinessRecordView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/8/15
 * @describle
 */
public class BusinessRecordPresenter extends WrapPresenter<BusinessRecordView> {
    Subscription mSubscription;
    BusinessRecordView mView;
    BusinessRecordService mService;
    @Override
    public void attachView(BusinessRecordView view) {
        mView = view;
        mService = ServiceFactory.getBusinessRecordService();
    }

    /**
     * 获取商务记录
     * @param token
     * @param friendId
     */
    public void getBusinessRecord(String token,int friendId){
        mSubscription = RXUtil.execute(mService.getBusinessRecord(token, friendId), new Observer<ResponseMessage<BusinessRecordModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<BusinessRecordModel> businessRecordModelResponseMessage) {
                if (businessRecordModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS){
                    mView.showTicket(businessRecordModelResponseMessage.data.ticketItem);
                    mView.showCoupon(businessRecordModelResponseMessage.data.couponItem);
                    mView.showActivity(businessRecordModelResponseMessage.data.activityItem);
                    mView.showSeeCar(businessRecordModelResponseMessage.data.seeItem);
                    mView.showInfo(businessRecordModelResponseMessage.data.basicInfoItem);
                }else {
                    CommonUtils.showToast(mView.getContext(),businessRecordModelResponseMessage.statusMessage);
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
