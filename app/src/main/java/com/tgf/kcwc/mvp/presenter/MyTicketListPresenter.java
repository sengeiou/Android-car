package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.MyTicketListModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketService;
import com.tgf.kcwc.mvp.view.MyTicketListView;
import com.tgf.kcwc.util.CommonUtils;
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

public class MyTicketListPresenter extends WrapPresenter<MyTicketListView> {
    private MyTicketListView mView;
    private Subscription     mSubscription;
    private TicketService    mService = null;

    @Override
    public void attachView(MyTicketListView view) {
        this.mView = view;
        mService = ServiceFactory.getTicketService();
    }

    public void loadMyTickets(String senseId, String id, String payStatus, String type,
                              String status, String token) {

        mSubscription = RXUtil.execute(
            mService.myTicketList(senseId, id, payStatus, type, status, token),
            new Observer<ResponseMessage<MyTicketListModel>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {
                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<MyTicketListModel> myTicketListModelResponseMessage) {

                    if (Constants.NetworkStatusCode.SUCCESS == myTicketListModelResponseMessage.statusCode) {
                        mView.showMyTickets(myTicketListModelResponseMessage.data);
                    } else {
                        CommonUtils.showToast(mView.getContext(),
                            myTicketListModelResponseMessage.statusMessage);
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
