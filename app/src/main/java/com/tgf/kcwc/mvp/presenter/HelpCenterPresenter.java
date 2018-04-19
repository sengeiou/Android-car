package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.HelpCenterModel;
import com.tgf.kcwc.mvp.model.HelpCenterService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.HelpCenterView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/8/22
 * @describle
 */
public class HelpCenterPresenter extends WrapPresenter<HelpCenterView>{
    HelpCenterView mView;
    Subscription mSubscription;
    HelpCenterService mService;
    @Override
    public void attachView(HelpCenterView view) {
        mView = view;
        mService = ServiceFactory.getHelpCenterService();
    }

    /**
     * 获取帮助中心列表
     * @param token
     * @param platformType
     * @param title
     */
    public void getHelpList(String token,String platformType,String title){
        mSubscription = RXUtil.execute(mService.getHelpList(token, platformType, title), new Observer<ResponseMessage<HelpCenterModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<HelpCenterModel> helpCenterModelResponseMessage) {
                if (helpCenterModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS){
                    mView.showHelpList(helpCenterModelResponseMessage.data);
                }else {
                    CommonUtils.showToast(mView.getContext(),helpCenterModelResponseMessage.statusMessage);
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
