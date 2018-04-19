package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.AboutTicketModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketOrderModel;
import com.tgf.kcwc.mvp.model.TicketRefundDetailModel;
import com.tgf.kcwc.mvp.model.TicketService;
import com.tgf.kcwc.mvp.view.TicketDataView;
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
 * 票务
 */

public class TicketDataPresenter extends WrapPresenter<TicketDataView> {
    private TicketDataView mView;
    private Subscription mSubscription;
    private TicketService mService = null;

    @Override
    public void attachView(TicketDataView view) {
        this.mView = view;
        mService = ServiceFactory.getTicketService();
    }

    public void getAboutTicket(String senseId, String token) {

        mSubscription = RXUtil.execute(mService.getAboutTicket(senseId, token),
                new Observer<ResponseMessage<AboutTicketModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<AboutTicketModel> responseMessage) {

                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            mView.showData(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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


    public void createRefund(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.createRefund(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            mView.showData(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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


    public void getTicketRefundDetail(String id, String token) {

        mSubscription = RXUtil.execute(mService.getTicketRefundDetail(id, token),
                new Observer<ResponseMessage<TicketRefundDetailModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<TicketRefundDetailModel> responseMessage) {

                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            mView.showData(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
     * 获取购票订单信息
     *
     * @param params
     */
    public void getTicketOrderDiscountInfo(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.getTicketOrderDiscountInfo(params),
                new Observer<ResponseMessage<TicketOrderModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<TicketOrderModel> responseMessage) {

                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            mView.showData(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
     * 票的转送
     *
     * @param params
     */
    public void setTicketForward(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.setTicketForward(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            mView.showData(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
