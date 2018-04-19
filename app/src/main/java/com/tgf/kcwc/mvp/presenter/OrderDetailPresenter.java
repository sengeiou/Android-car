package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.OrderModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketService;
import com.tgf.kcwc.mvp.view.OrderDetailView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;

/**
 * Author：Jenny
 * Date:2016/12/14 15:55
 * E-mail：fishloveqin@gmail.com
 * 订单
 */

public class OrderDetailPresenter extends WrapPresenter<OrderDetailView> {
    private OrderDetailView mView;
    private Subscription    mSubscription;
    private TicketService   mService = null;

    @Override
    public void attachView(OrderDetailView view) {
        this.mView = view;
        mService = ServiceFactory.getTicketService();
    }

    public void loadOrderDetails(String id, String token) {

        mSubscription = RXUtil.execute(mService.getOrderDetails(id,token),
            new Observer<ResponseMessage<OrderModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<OrderModel> orderModelResponseMessage) {

                    mView.showOrderDetails(orderModelResponseMessage.data);
                }
            });
        mSubscriptions.add(mSubscription);
    }
}
