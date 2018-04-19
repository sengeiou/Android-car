package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.UserManagerService;
import com.tgf.kcwc.mvp.view.UserDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:20175/15 13:55
 * E-mail：fishloveqin@gmail.com
 * 会员中心-首页
 */

public class UserChangePresenter extends WrapPresenter<UserDataView> {
    private UserDataView mView;
    private UserManagerService mService = null;

    @Override
    public void attachView(UserDataView view) {
        this.mView = view;
        mService = ServiceFactory.getUMService();
    }

    /**
     * 会员中心-更换背景图/头像
     */
    public void getReplacepic(String token, String type, String url) {

        RXUtil.execute(mService.getReplacepic(token, type, url), new Observer<ResponseMessage<Account.UserInfo>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.showToast(mView.getContext(), "网络异常，稍候再试！");
            }

            @Override
            public void onNext(ResponseMessage<Account.UserInfo> baseBean) {
                if (baseBean.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showDatas(baseBean.data);
                } else {
                    CommonUtils.showToast(mView.getContext(), baseBean.statusMessage);
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
