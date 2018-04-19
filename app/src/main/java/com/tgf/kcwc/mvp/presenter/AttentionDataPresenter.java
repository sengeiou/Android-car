package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.GeneralizationService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.AttentionView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2017/2/13 21:06
 * E-mail：fishloveqin@gmail.com
 */

public class AttentionDataPresenter extends WrapPresenter<AttentionView> {

    private AttentionView mView;
    private Subscription mSubscription;
    private GeneralizationService mService = null;

    @Override
    public void attachView(AttentionView view) {
        this.mView = view;
        mService = ServiceFactory.getGeneralizationService();
    }

    /**
     * 关注
     */
    public void execAttention(String followId, String token) {

        mSubscription = RXUtil.execute(mService.executeAttention(followId, token),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> listResponseMessage) {

                        if (listResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                            mView.showAddAttention("");
                        } else {
                            CommonUtils.showToast(mView.getContext(),
                                    listResponseMessage.statusMessage);
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

    /**
     * 关注加备注
     */
    public void execAttention(String followId, String token, String name) {

        mSubscription = RXUtil.execute(mService.executeAttention(followId, token, name),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> listResponseMessage) {

                        if (listResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                            mView.showAddAttention("");
                        } else {
                            CommonUtils.showToast(mView.getContext(),
                                    listResponseMessage.statusMessage);
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

    /**
     *  取消关注
     */
    public void cancelAttention(String followId, String token) {

        mSubscription = RXUtil.execute(mService.cancelAttention(followId, token),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> listResponseMessage) {

                        if (listResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                            mView.showCancelAttention("");
                        } else {
                            CommonUtils.showToast(mView.getContext(),
                                    listResponseMessage.statusMessage);
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
