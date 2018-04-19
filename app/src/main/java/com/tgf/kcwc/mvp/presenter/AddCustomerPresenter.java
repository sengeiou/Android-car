package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.AddCustomerModel;
import com.tgf.kcwc.mvp.model.AddCustomerService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.AddCustomerView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/8/8 14:33
 */

public class AddCustomerPresenter extends WrapPresenter<AddCustomerView>{
    AddCustomerService mService;
    AddCustomerView mView;
    Subscription mSubscription;
    @Override
    public void attachView(AddCustomerView view) {
        mView = view;
        mService = ServiceFactory.getAddCustomerService();
    }
    public void addCustomer(String token,String name,String tel,int friendGroupId){
        mSubscription = RXUtil.execute(mService.addFriend(token,name, tel, friendGroupId), new Observer<ResponseMessage<AddCustomerModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<AddCustomerModel> addCustomerModelResponseMessage) {
                if (addCustomerModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS){
                    mView.addSuccess(addCustomerModelResponseMessage.data.list);
                }else{
                    mView.addFail(addCustomerModelResponseMessage.statusMessage);
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
