package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.AddCustomerModel;
import com.tgf.kcwc.mvp.model.GroupMoveService;
import com.tgf.kcwc.mvp.view.GroupMoveView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor Administrator
 * @time 2017/8/9
 * @describle
 */
public class GroupMovePresenter extends WrapPresenter<GroupMoveView> {
    GroupMoveService mService;
    GroupMoveView mView;
    Subscription mSubscription;
    @Override
    public void attachView(GroupMoveView view) {
        mView = view;
        mService = ServiceFactory.getGroupMoveService();
    }

    /**
     * 组间移动
     * @param token
     * @param name
     * @param tel
     * @param friendGroupId
     */
    public void groupMove(String token,String name,String tel,int friendGroupId){
        final int position = 0;
        mSubscription = RXUtil.execute(mService.moveFriend(token, name, tel, friendGroupId), new Observer<AddCustomerModel>() {
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
                    mView.moveSuccess(addCustomerModel,position);
                }else {
                    mView.moveFail(addCustomerModel.statusMessage);
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
