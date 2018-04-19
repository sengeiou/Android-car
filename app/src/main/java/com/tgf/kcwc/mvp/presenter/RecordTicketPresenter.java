package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BusinessRecordDetailService;
import com.tgf.kcwc.mvp.model.RecordTicketModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.RecordCouponView;
import com.tgf.kcwc.mvp.view.RecordTicketView;
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
public class RecordTicketPresenter extends WrapPresenter<RecordTicketView>{
    Subscription mSubscription;
    BusinessRecordDetailService mService;
    RecordTicketView mView;

    @Override
    public void attachView(RecordTicketView view) {
        mView = view;
        mService = ServiceFactory.getBusinessRecordDetailService();
    }

    /**
     * 获取票证
     * @param friendId
     * @param type
     */
    public void getTicket(String token , int friendId,int type,int page){
        mSubscription = RXUtil.execute(mService.getTicket(token,friendId, type,page), new Observer<ResponseMessage<RecordTicketModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<RecordTicketModel> recordTicketModelResponseMessage) {
                if (recordTicketModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS){
                    mView.showTicket(recordTicketModelResponseMessage.data.list);
                    mView.showTicketHead(recordTicketModelResponseMessage.data.userItem);
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
