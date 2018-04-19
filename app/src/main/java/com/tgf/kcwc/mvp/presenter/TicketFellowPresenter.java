package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketFellowModel;
import com.tgf.kcwc.mvp.model.TicketManageService;
import com.tgf.kcwc.mvp.view.TicketFellowView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/2/9 0009
 * E-mail:hekescott@qq.com
 */

public class TicketFellowPresenter extends WrapPresenter<TicketFellowView> {
    private TicketFellowView mView;
    private TicketManageService mService;
    private Subscription     mSubscription;

    @Override
    public void attachView(TicketFellowView view) {
        mView = view;
        mService = ServiceFactory.getTicketManageService();
    }

    public void getTicketFellow(String token, int id, String mobile, int userType) {
        mSubscription = RXUtil.execute(mService.getTicketFellow(token, id, mobile, userType),
            new Observer<ResponseMessage<TicketFellowModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ResponseMessage<TicketFellowModel> ticketFellowModelResponseMessage) {
                    mView.showTickeFellow(ticketFellowModelResponseMessage.getData());
                }
            });
    }
    public void  sendTicket(String token, int tfId, final String mobile, String name, int nums, int user_type){
        RXUtil.execute(
                mService.sendTicket(token, tfId + "", 0, mobile, name, nums, 2, 1, user_type),
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
                            mView.faildeMessage(responseMessage.statusMessage);
                        }
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                });
    }


    @Override
    public void detachView() {
        unsubscribe();
    }

    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
