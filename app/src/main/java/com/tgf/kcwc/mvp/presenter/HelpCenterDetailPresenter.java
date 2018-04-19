package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.HelpCenterDetailModel;
import com.tgf.kcwc.mvp.model.HelpCenterDetailService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.HelpCenterDetailView;
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
public class HelpCenterDetailPresenter extends WrapPresenter<HelpCenterDetailView> {
    HelpCenterDetailService mService;
    HelpCenterDetailView mView;
    Subscription mSubscription;
    @Override
    public void attachView(HelpCenterDetailView view) {
        mView = view;
        mService = ServiceFactory.getHelpCenterDetailService();
    }
    public void getHelpDetail(String token,int id){
        mSubscription = RXUtil.execute(mService.getHelpDetail(token, id), new Observer<ResponseMessage<HelpCenterDetailModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<HelpCenterDetailModel> helpCenterDetailModelResponseMessage) {
                if (helpCenterDetailModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS){
                    mView.showHelpCenterDetail(helpCenterDetailModelResponseMessage.data);
                }else {
                    CommonUtils.showToast(mView.getContext(),helpCenterDetailModelResponseMessage.statusMessage);
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
