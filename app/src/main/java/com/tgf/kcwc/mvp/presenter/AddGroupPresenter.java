package com.tgf.kcwc.mvp.presenter;

import android.util.Log;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.AddGroupModel;
import com.tgf.kcwc.mvp.model.AddGroupService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.AddGroupView;
import com.tgf.kcwc.mvp.view.WrapView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/8/8 17:32
 */

public class AddGroupPresenter extends WrapPresenter<AddGroupView> {
    AddGroupService mService;
    AddGroupView mView;
    Subscription mSubscription;

    @Override
    public void attachView(AddGroupView view) {
        mView = view;
        mService = ServiceFactory.getAddGroupService();
    }

    /**
     * 增加分组名
     *
     * @param name
     */
    public void addGroup(String token, String name) {
        mSubscription = RXUtil.execute(mService.addGroup(token, name), new Observer<AddGroupModel>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(AddGroupModel addGroupModel) {
                if (addGroupModel.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.addGroupSuccess(addGroupModel.list);
                } else {
                    mView.addGroupFail(addGroupModel.statusMessage);
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
