package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketExhibitModel;
import com.tgf.kcwc.mvp.model.TicketManageService;
import com.tgf.kcwc.mvp.view.TicketExhibitView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/2/7 0007
 * E-mail:hekescott@qq.com
 */

public class TicketExhibitPresenter extends WrapPresenter<TicketExhibitView> {
    private TicketManageService mService;
    private TicketExhibitView mView;
    private Subscription      mSubscription;

    @Override
    public void attachView(TicketExhibitView view) {
        mService = ServiceFactory.getTicketManageService();
        mView = view;
    }

    public void  getTicketExhibitInfo(String token,int id){
        mSubscription =  RXUtil.execute(mService.getTicketExhibitInfo(token, id), new Observer<ResponseMessage<TicketExhibitModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<TicketExhibitModel> ticketExhibitModelResponseMessage) {
                mView.showTicketExhibitInfo(ticketExhibitModelResponseMessage.getData().event);
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
