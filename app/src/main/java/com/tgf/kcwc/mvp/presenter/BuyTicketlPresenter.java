package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BuyTicketModel;
import com.tgf.kcwc.mvp.model.Order;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketService;
import com.tgf.kcwc.mvp.view.BuyTicketView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/14 15:55
 * E-mail：fishloveqin@gmail.com
 * 购票Presenter
 */

public class BuyTicketlPresenter extends WrapPresenter<BuyTicketView> {
    private BuyTicketView mView;
    private Subscription mSubscription;
    private TicketService mService = null;

    @Override
    public void attachView(BuyTicketView view) {
        this.mView = view;
        mService = ServiceFactory.getTicketService();
    }

    /**
     * @param id      门票id
     * @param eventId 展会id
     * @param token   用户token
     */
    public void loadTickets(String id, String eventId, final String token) {

        mSubscription = RXUtil.execute(mService.buyTicketList(id, eventId, token),
                new Observer<ResponseMessage<BuyTicketModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<BuyTicketModel> buyTicketModelResponseMessage) {
                        if (buyTicketModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                            mView.showBuyTickets(buyTicketModelResponseMessage.data);
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

    public void createOrder(final String tIds, final String nums, final String token, String type) {

        mSubscription = RXUtil.execute(mService.generateOrder(tIds, nums, token, type),
                new Observer<ResponseMessage<Order>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();

                    }

                    @Override
                    public void onNext(ResponseMessage<Order> respMsg) {

                        if (respMsg.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                            mView.generateOrderSuccess(respMsg.data.orderId);
                        } else {
                            mView.generateOrderFailure(respMsg.statusMessage);

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
