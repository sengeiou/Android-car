package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketManageService;
import com.tgf.kcwc.mvp.model.TicketSendRecordModel;
import com.tgf.kcwc.mvp.view.SendRecordView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/2/8 0008
 * E-mail:hekescott@qq.com
 */

public class SendRecordPresenter extends WrapPresenter<SendRecordView> {
    private SendRecordView mView;
    private TicketManageService mService;

    @Override
    public void attachView(SendRecordView view) {
        mView = view;
        mService = ServiceFactory.getTicketManageService();
    }

    public void getSendTicketRecord(String token, int id) {
      Subscription subscription= RXUtil.execute(mService.getTicketSendRecord(token, id), new Observer<ResponseMessage<TicketSendRecordModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<TicketSendRecordModel> ticketRecordModelResponseMessage) {
                   TicketSendRecordModel ticketRecordModel =     ticketRecordModelResponseMessage.getData();
                mView.showSendRecorList(ticketRecordModel.recordItems);
            }
        });
        mSubscriptions.add(subscription);
    }
}
