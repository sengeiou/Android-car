package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketDetailModel;
import com.tgf.kcwc.mvp.model.TicketService;
import com.tgf.kcwc.mvp.view.FreeTicketDetailView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/14 15:55
 * E-mail：fishloveqin@gmail.com
 * 我的门票Presenter
 */

public class FreeTicketDetailPresenter extends WrapPresenter<FreeTicketDetailView> {
    private FreeTicketDetailView mView;
    private Subscription         mSubscription;
    private TicketService        mService = null;

    @Override
    public void attachView(FreeTicketDetailView view) {
        this.mView = view;
        mService = ServiceFactory.getTicketService();
    }

    public void loadFreeTicketDetail(String ids, String token) {

        mSubscription = RXUtil.execute(mService.freeTicketDetail(ids, token),
            new Observer<ResponseMessage<TicketDetailModel>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {
                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<TicketDetailModel> responseMessage) {

                    mView.showFreeTicketDetail(responseMessage.data);
                }
            }, new Action0() {
                @Override
                public void call() {
                    mView.setLoadingIndicator(true);
                }
            });
        mSubscriptions.add(mSubscription);
    }

    public void receiveTickets(String ids, String token) {

        mSubscription = RXUtil.execute(mService.receiveTickets(ids, token),
            new Observer<ResponseMessage<Object>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ResponseMessage<Object> responseBody) {

                    if (responseBody.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                        mView.receiveTicket(true);
                    } else {
                        mView.receiveTicket(false);
                    }
                }
            });
        mSubscriptions.add(mSubscription);
    }
}
