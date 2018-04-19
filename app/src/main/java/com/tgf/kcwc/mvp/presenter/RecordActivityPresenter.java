package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BusinessRecordDetailService;
import com.tgf.kcwc.mvp.model.RecordActivityModel;
import com.tgf.kcwc.mvp.model.RecordTicketModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.RecordActivityView;
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
public class RecordActivityPresenter extends WrapPresenter<RecordActivityView>{
    Subscription mSubscription;
    BusinessRecordDetailService mService;
    RecordActivityView mView;
    @Override
    public void attachView(RecordActivityView view) {
        mView = view;
        mService = ServiceFactory.getBusinessRecordDetailService();
    }
    /**
     * 获取活动
     * @param friendId
     * @param type
     */
    public void getActivity(String token , int friendId,int type,int page){
        mSubscription = RXUtil.execute(mService.getActivity(token,friendId, type,page), new Observer<ResponseMessage<RecordActivityModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<RecordActivityModel> recordActivityModelResponseMessage) {
                if (recordActivityModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS){
                    mView.showActivity(recordActivityModelResponseMessage.data.list);
                    mView.showActivityHead(recordActivityModelResponseMessage.data.userItem);
                }else {
                    CommonUtils.showToast(mView.getContext(),recordActivityModelResponseMessage.statusMessage);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

}
