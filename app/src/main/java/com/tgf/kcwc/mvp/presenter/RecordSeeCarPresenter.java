package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BusinessRecordDetailService;
import com.tgf.kcwc.mvp.model.RecordCouponModel;
import com.tgf.kcwc.mvp.model.RecordSeeCarModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.RecordCouponView;
import com.tgf.kcwc.mvp.view.RecordSeeCarView;
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
public class RecordSeeCarPresenter extends WrapPresenter<RecordSeeCarView>{
    Subscription mSubscription;
    BusinessRecordDetailService mService;
    RecordSeeCarView mView;
    @Override
    public void attachView(RecordSeeCarView view) {
        mView = view;
        mService = ServiceFactory.getBusinessRecordDetailService();
    }
    /**
     * 获取看车
     * @param friendId
     * @param type
     */
    public void getSeeCar(String token,int friendId,int type,int page){
        mSubscription = RXUtil.execute(mService.getSeeCar(token,friendId, type,page), new Observer<ResponseMessage<RecordSeeCarModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<RecordSeeCarModel> recordTicketModelResponseMessage) {
                if (recordTicketModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS){
                    mView.showSeeCar(recordTicketModelResponseMessage.data.list);
                    mView.showSeeCarHead(recordTicketModelResponseMessage.data.userItem);
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
