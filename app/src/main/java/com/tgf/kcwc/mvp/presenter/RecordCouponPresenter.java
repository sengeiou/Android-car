package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BusinessRecordDetailService;
import com.tgf.kcwc.mvp.model.RecordActivityModel;
import com.tgf.kcwc.mvp.model.RecordCouponModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.RecordActivityView;
import com.tgf.kcwc.mvp.view.RecordCouponView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/8/14
 * @describle
 */
public class RecordCouponPresenter extends WrapPresenter<RecordCouponView>{
    Subscription mSubscription;
    BusinessRecordDetailService mService;
    RecordCouponView mView;
    @Override
    public void attachView(RecordCouponView view) {
        mView = view;
        mService = ServiceFactory.getBusinessRecordDetailService();
    }
    /**
     * 获取代金卷
     * @param friendId
     * @param type
     */
    public void getCoupon(String token , int friendId,int type,int page){
        mSubscription = RXUtil.execute(mService.getCoupon(token,friendId, type,page), new Observer<ResponseMessage<RecordCouponModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<RecordCouponModel> recordTicketModelResponseMessage) {
                if (recordTicketModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS){
                    mView.showCoupon(recordTicketModelResponseMessage.data.list);
                    mView.showCouponHead(recordTicketModelResponseMessage.data.userItem);
                }else {
                    CommonUtils.showToast(mView.getContext(),recordTicketModelResponseMessage.statusMessage);
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
