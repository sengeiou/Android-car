package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ActionRecordModel;
import com.tgf.kcwc.mvp.model.ActionRecordService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.ActionRecordView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/8/1 11:21
 */

public class ActionRecordPresenter extends WrapPresenter<ActionRecordView>{
    ActionRecordView mView;
    ActionRecordService mService;
    Subscription mSubscription;
    @Override
    public void attachView(ActionRecordView view) {
        mView = view;
        mService = ServiceFactory.getActionRecordService();
    }
    public void getActionRecord(String token,int friendId,int page){
        mSubscription = RXUtil.execute(mService.getActionRecord(token, friendId,page), new Observer<ResponseMessage<ActionRecordModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<ActionRecordModel> actionRecordModelResponseMessage) {
                if (actionRecordModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS){
                    mView.showActionRecord(actionRecordModelResponseMessage.data);
                }else {
                    CommonUtils.showToast(mView.getContext(),actionRecordModelResponseMessage.statusMessage);
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
