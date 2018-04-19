package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketManageService;
import com.tgf.kcwc.mvp.model.TicketSendOrgRecordModel;
import com.tgf.kcwc.mvp.model.TicketSendRecordModel;
import com.tgf.kcwc.mvp.view.SendOrgRecordView;
import com.tgf.kcwc.mvp.view.SendRecordView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/2/8 0008
 * E-mail:hekescott@qq.com
 */

public class SendOrgRecordPresenter extends WrapPresenter<SendOrgRecordView> {
    private SendOrgRecordView mView;
    private TicketManageService mService;

    @Override
    public void attachView(SendOrgRecordView view) {
        mView = view;
        mService = ServiceFactory.getTicketManageService();
    }

    public void getSendOrgTicketRecord(String token, int id) {
      Subscription subscription= RXUtil.execute(mService.getTicketOrgSendRecord(token, id), new Observer<ResponseMessage<TicketSendOrgRecordModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<TicketSendOrgRecordModel> ticketRecordModelResponseMessage) {
                TicketSendOrgRecordModel ticketRecordModel =     ticketRecordModelResponseMessage.getData();
                mView.showSendOrgRecorList(ticketRecordModel.list);

            }
        });
        mSubscriptions.add(subscription);
    }
}
