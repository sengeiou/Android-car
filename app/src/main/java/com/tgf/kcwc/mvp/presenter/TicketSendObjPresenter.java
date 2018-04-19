package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketManageService;
import com.tgf.kcwc.mvp.model.TicketSendObjModel;
import com.tgf.kcwc.mvp.view.TicketSendObjView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/2/7 0007
 * E-mail:hekescott@qq.com
 */

public class TicketSendObjPresenter extends WrapPresenter<TicketSendObjView> {
    private TicketSendObjView mView;
    private TicketManageService mService;
    private Subscription      mSubscription;

    @Override
    public void attachView(TicketSendObjView view) {
        mView = view;
        mService = ServiceFactory.getTicketManageService();
    }

    public void getTicketSendObj(String token, int id) {
        mSubscription =   RXUtil.execute(mService.getTicketSendObj(token, id),
                new Observer<ResponseMessage<TicketSendObjModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(ResponseMessage<TicketSendObjModel> sendObjModelResponseMessage) {
                        mView.showSendUserList(sendObjModelResponseMessage.getData().ticketUser);
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                }
        );
    }

    public void sendTicket(String token, int tfId, String mobile, String name, int nums, int user_type, final int pos) {
        RXUtil.execute(
            mService.sendTicket(token,tfId+"", 0, mobile, name,nums, 2,1, user_type),
            new Observer<ResponseMessage>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ResponseMessage responseMessage) {
                    if(responseMessage.statusCode ==0){
                        mView.showSendTicketSuccess(pos);
                    }else {
                        mView.faildeMessage(responseMessage.statusMessage);
                    }
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
