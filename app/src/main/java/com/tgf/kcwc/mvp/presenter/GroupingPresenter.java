package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.AddCustomerModel;
import com.tgf.kcwc.mvp.model.GroupingService;
import com.tgf.kcwc.mvp.view.AddCustomerView;
import com.tgf.kcwc.mvp.view.AddGroupView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor Administrator
 * @time 2017/8/10
 * @describle
 */
public class GroupingPresenter extends WrapPresenter<AddCustomerView> {
    Subscription mSubscription;
    AddCustomerView mView;
    GroupingService mService;
    @Override
    public void attachView(AddCustomerView view) {
        mView = view;
        mService = ServiceFactory.getGroupingService();
    }

    /**
     * 好友分组
     * @param token
     * @param friendGroupId
     * @param ids
     */
    public void grouping(String token,int friendGroupId,String ids){
        mSubscription = RXUtil.execute(mService.grouping(token, friendGroupId, ids), new Observer<AddCustomerModel>() {
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
        mSubscriptions.add(mSubscription);
    }
}
