package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.AddCustomerModel;
import com.tgf.kcwc.mvp.model.DeleteGroupService;
import com.tgf.kcwc.mvp.view.GroupMoveView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor Administrator
 * @time 2017/8/10
 * @describle
 */
public class DeleteGroupPresenter extends WrapPresenter<GroupMoveView> {
    Subscription mSubscription;
    DeleteGroupService mService;
    GroupMoveView mView;

    @Override
    public void attachView(GroupMoveView view) {
        mView = view;
        mService = ServiceFactory.getDeleteGroupService();
    }

    /**
     * 删除分组
     */
    public void deleteGroup(String token, int groupId, final int position) {
        mSubscription = RXUtil.execute(mService.deleteGroup(token, groupId), new Observer<AddCustomerModel>() {
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
