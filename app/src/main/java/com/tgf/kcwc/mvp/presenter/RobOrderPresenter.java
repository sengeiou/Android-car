package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.AddCustomerModel;
import com.tgf.kcwc.mvp.model.RobOrderService;
import com.tgf.kcwc.mvp.view.AddCustomerView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor Administrator
 * @time 2017/8/11
 * @describle
 */
public class RobOrderPresenter extends WrapPresenter<AddCustomerView>{
    Subscription mSubscription;
    RobOrderService mService;
    AddCustomerView mView;
    @Override
    public void attachView(AddCustomerView view) {
        mView = view;
        mService = ServiceFactory.getRobOrderService();
    }

    /**
     * 潜客抢单
     * @param userId
     */
    public void rob(String token,String userId){
        mSubscription = RXUtil.execute(mService.rob(token,userId), new Observer<AddCustomerModel>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(AddCustomerModel addCustomerModel) {
                if (addCustomerModel.statusCode == Constants.NetworkStatusCode.SUCCESS){
                    mView.addSuccess(addCustomerModel.list);
                }else {
                    mView.addFail(addCustomerModel.statusMessage);
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
