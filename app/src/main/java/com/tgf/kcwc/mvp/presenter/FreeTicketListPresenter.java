package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.FreeTicketListModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketService;
import com.tgf.kcwc.mvp.view.FreeTicketListView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

import static com.baidu.location.f.mC;

/**
 * Author：Jenny
 * Date:2016/12/14 15:55
 * E-mail：fishloveqin@gmail.com
 * 我的门票Presenter
 */

public class FreeTicketListPresenter extends WrapPresenter<FreeTicketListView> {
    private FreeTicketListView mView;
    private Subscription       mSubscription;
    private TicketService      mService = null;

    @Override
    public void attachView(FreeTicketListView view) {
        this.mView = view;
        mService = ServiceFactory.getTicketService();
    }

    public void loadFreeTickets(String senseId, String id, String status, String token) {

        mSubscription = RXUtil.execute(mService.freeTicketList(senseId, id, status, token),
            new Observer<ResponseMessage<FreeTicketListModel>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {
                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<FreeTicketListModel> freeTicketListModelResponseMessage) {

                    if (Constants.NetworkStatusCode.SUCCESS == freeTicketListModelResponseMessage.statusCode) {
                        mView.showFreeTickets(freeTicketListModelResponseMessage.data);
                    } else {
                        CommonUtils.showToast(mView.getContext(),
                            freeTicketListModelResponseMessage.statusMessage);
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
