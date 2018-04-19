package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ChangeExhibitionModel;
import com.tgf.kcwc.mvp.model.ChangeExhibitionService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.ChangeExhibitionView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/11/16.
 */

public class ChangeExhibitionPresenter extends WrapPresenter<ChangeExhibitionView> {
    ChangeExhibitionView mView;
    ChangeExhibitionService mService;
    Subscription mSubscription;

    @Override
    public void attachView(ChangeExhibitionView view) {
        mService = ServiceFactory.getChangeExhibitionService();
        mView = view;
    }

    /**
     * 改变展位
     * @param token
     * @param applyId
     */
    public void changeExhibition(String token, int applyId) {
        mSubscription = RXUtil.execute(mService.changeExhibition(token, applyId), new Observer<ResponseMessage<ChangeExhibitionModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<ChangeExhibitionModel> changeExhibitionModelResponseMessage) {
                if (changeExhibitionModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showExhibitionPos(changeExhibitionModelResponseMessage.data);
                } else {
                    CommonUtils.showToast(mView.getContext(),changeExhibitionModelResponseMessage.statusMessage);
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
