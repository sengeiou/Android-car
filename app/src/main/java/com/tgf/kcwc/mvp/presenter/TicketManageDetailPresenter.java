package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketManageDetailModel;
import com.tgf.kcwc.mvp.model.TicketManageService;
import com.tgf.kcwc.mvp.view.TicketManageDetailView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/2/7 0007
 * E-mail:hekescott@qq.com
 */

public class TicketManageDetailPresenter extends WrapPresenter<TicketManageDetailView> {
    private Subscription           mSubscription;
    private TicketManageService mSevice;
    private TicketManageDetailView mView;

    @Override
    public void attachView(TicketManageDetailView view) {
        mSevice = ServiceFactory.getTicketManageService();
        mView = view;
    }

    @Override
    public void detachView() {
        unsubscribe();
    }

    public void getTicketManageDetail(String token, int id) {

        mSubscription = RXUtil.execute(mSevice.getTicketManageDetail(token, id, 1),
            new Observer<ResponseMessage<TicketManageDetailModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    mView.failedMessage(e.getLocalizedMessage());
                }

                @Override
                public void onNext(ResponseMessage<TicketManageDetailModel> ticketManageDetailModelResponseMessage) {
                    mView.showTicketDetail(ticketManageDetailModelResponseMessage.getData().mDetail);
                }
            });

    }

    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

    }
}
