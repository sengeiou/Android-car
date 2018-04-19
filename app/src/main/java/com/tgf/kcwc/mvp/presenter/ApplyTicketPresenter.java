package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ApplyTicketModel;
import com.tgf.kcwc.mvp.model.PreTicketModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketDescModel;
import com.tgf.kcwc.mvp.model.TicketService;
import com.tgf.kcwc.mvp.view.WrapApplyTicketView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/14 15:55
 * E-mail：fishloveqin@gmail.com
 * 预报名
 */

public class ApplyTicketPresenter extends WrapPresenter<WrapApplyTicketView> {
    private WrapApplyTicketView mView;
    private Subscription mSubscription;
    private TicketService mService = null;

    @Override
    public void attachView(WrapApplyTicketView view) {
        this.mView = view;
        mService = ServiceFactory.getTicketService();
    }


    /**
     * 获取申请的表单信息
     *
     * @param senseId
     * @param type
     * @param channelId
     * @param id
     * @param tid
     * @param token
     */
    public void getApplyForms(String senseId, String type, String channelId, String id,
                              String tid, String token) {

        mSubscription = RXUtil.execute(
                mService.preRegTicketList(senseId, type, channelId, id, tid, token),
                new Observer<ResponseMessage<PreTicketModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<PreTicketModel> dataMsg) {

                        mView.showForms(dataMsg.data);
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
     * 获取参展列表
     *
     * @param params
     */
    public void getApplyList(Map<String, String> params, boolean isTicket) {

        if(isTicket){
            mSubscription = RXUtil.execute(
                    mService.getApplyList(params),
                    new Observer<ResponseMessage<ApplyTicketModel>>() {
                        @Override
                        public void onCompleted() {

                            mView.setLoadingIndicator(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.showLoadingTasksError();
                        }

                        @Override
                        public void onNext(ResponseMessage<ApplyTicketModel> dataMsg) {

                            mView.showApplyList(dataMsg.data);
                        }
                    }, new Action0() {
                        @Override
                        public void call() {

                            mView.setLoadingIndicator(true);
                        }
                    });
            mSubscriptions.add(mSubscription);
        }else {
            mSubscription = RXUtil.execute(
                    mService.getCertApplyList(params),
                    new Observer<ResponseMessage<ApplyTicketModel>>() {
                        @Override
                        public void onCompleted() {

                            mView.setLoadingIndicator(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.showLoadingTasksError();
                        }

                        @Override
                        public void onNext(ResponseMessage<ApplyTicketModel> dataMsg) {

                            mView.showApplyList(dataMsg.data);
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


    /**
     * 获取参展列表
     *
     * @param params
     */
    public void getADInfo(Map<String, String> params) {

        mSubscription = RXUtil.execute(
                mService.getADInfo(params),
                new Observer<ResponseMessage<String>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<String> dataMsg) {

                        mView.showAD(dataMsg.data);
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
     * 获取票证描述信息
     *
     * @param params
     */
    public void getTicketDescInfo(Map<String, String> params) {

        mSubscription = RXUtil.execute(
                mService.getTicketDescInfo(params),
                new Observer<ResponseMessage<TicketDescModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<TicketDescModel> dataMsg) {

                        if (dataMsg.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                            mView.showTicketDesc(dataMsg.data);
                        } else {

                            CommonUtils.showToast(mView.getContext(), dataMsg.statusMessage);
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
