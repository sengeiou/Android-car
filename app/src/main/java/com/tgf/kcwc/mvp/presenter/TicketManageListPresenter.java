package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketManageListModel;
import com.tgf.kcwc.mvp.model.TicketManageService;
import com.tgf.kcwc.mvp.view.TicketManageListView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/2/6 0006
 * E-mail:hekescott@qq.com
 */

public class TicketManageListPresenter extends WrapPresenter<TicketManageListView> {

    private Subscription         mSubscription;
    private TicketManageService mSevice;
    private TicketManageListView mView;

    @Override
    public void attachView(TicketManageListView view) {
        mSevice = ServiceFactory.getTicketManageService();
        mView = view;
    }

    @Override
    public void detachView() {
        unsubscribe();
    }

    public void getManageTicketList(String token) {
        mSubscription =  RXUtil.execute(mSevice.getTicketManageList(token),
            new Observer<ResponseMessage<TicketManageListModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ResponseMessage<TicketManageListModel> ticketManageListModelResponseMessage) {
                    mView.showTicketList(ticketManageListModelResponseMessage.getData());
                }
            });

    }

    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
