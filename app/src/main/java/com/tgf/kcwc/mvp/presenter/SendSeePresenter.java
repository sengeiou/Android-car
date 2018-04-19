package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CheckSendSeeModel;
import com.tgf.kcwc.mvp.model.ManageCouponService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketExhibitModel;
import com.tgf.kcwc.mvp.model.TicketManageService;
import com.tgf.kcwc.mvp.view.TicketSendSeeView;
import com.tgf.kcwc.util.RXUtil;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/2/13 0013
 * E-mail:hekescott@qq.com
 */

public class SendSeePresenter extends WrapPresenter<TicketSendSeeView> {
    private TicketSendSeeView mView;
    private TicketManageService mService;

    @Override
    public void attachView(TicketSendSeeView view) {
        mView = view;
        mService = ServiceFactory.getTicketManageService();
    }

    public void getTicketExhibitInfo(String token, int id) {
        Subscription mSubscription = RXUtil.execute(mService.getTicketExhibitInfo(token, id), new Observer<ResponseMessage<TicketExhibitModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<TicketExhibitModel> ticketExhibitModelResponseMessage) {
                mView.showSendSeehead(ticketExhibitModelResponseMessage.getData().event);
            }
        });
        mSubscriptions.add(mSubscription);
    }

    public void sendTicket(String token, int tfId, String mobile, String name, String nums,int timelimit, String user_type) {
        RXUtil.execute(
                mService.sendTicket(token, tfId + "", 0, mobile, name, nums,timelimit, 2, user_type),
                new Observer<ResponseMessage>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseMessage responseMessage) {
                        if (responseMessage.statusCode == 0) {
                            mView.showSendTicketSuccess();
                        } else {
                            mView.failedMessage(responseMessage.statusMessage);
                        }
                    }
                });
    }
    public void sendTicket( String token,  String tfId,String nums, String uid) {
        RXUtil.execute(
                mService.sendTicket(token, tfId + "", nums, uid),
                new Observer<ResponseMessage>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseMessage responseMessage) {
                        if (responseMessage.statusCode == 0) {
                            mView.showSendTicketSuccess();
                        } else {
                            mView.failedMessage(responseMessage.statusMessage);
                        }
                    }
                });
    }

    public void sendSignTicket(String token, int tfId, String mobile, String sign, String nums ) {
        RXUtil.execute(
                mService.sendTicket(token, tfId + "", 0, mobile, sign, nums, 2, 1+""),
                new Observer<ResponseMessage>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(ResponseMessage responseMessage) {
                        if (responseMessage.statusCode == 0) {
                            mView.showSendTicketSuccess();
                        } else {
                            mView.failedMessage(responseMessage.statusMessage);
                        }
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                });
    }

    public void checkeSendSeeUser(String token, int tfid, String mobile) {
        RXUtil.execute(mService.checkSendSeeUser(token, tfid, mobile), new Observer<ResponseMessage<ArrayList<CheckSendSeeModel>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<ArrayList<CheckSendSeeModel>> checkSendSeeModelResponseMessage) {
                mView.showCheckTicket(checkSendSeeModelResponseMessage.getData());
            }
        });
    }

}
