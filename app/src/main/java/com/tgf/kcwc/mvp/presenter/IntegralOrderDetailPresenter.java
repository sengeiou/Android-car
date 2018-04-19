package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.IntegralOrderDetailBean;
import com.tgf.kcwc.mvp.model.IntegralService;
import com.tgf.kcwc.mvp.model.OrderModel;
import com.tgf.kcwc.mvp.model.OrderPayParam;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketService;
import com.tgf.kcwc.mvp.view.IntegralOrderDetailView;
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

public class IntegralOrderDetailPresenter extends WrapPresenter<IntegralOrderDetailView> {
    private IntegralOrderDetailView mView;
    private Subscription mSubscription;
    private IntegralService mService = null;

    @Override
    public void attachView(IntegralOrderDetailView view) {
        this.mView = view;
        mService = ServiceFactory.getIntegralService();
    }

    public void loadOrderDetails(String id, String token) {

        mSubscription = RXUtil.execute(mService.getOrderRecordsDetail(id, token),
                new Observer<ResponseMessage<IntegralOrderDetailBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<IntegralOrderDetailBean> orderModelResponseMessage) {

                        if (orderModelResponseMessage.statusCode == 0) {
                            mView.showOrderDetails(orderModelResponseMessage.data);
                        } else {
                            mView.dataDefeated(orderModelResponseMessage.statusMessage);
                        }
                    }
                });
        mSubscriptions.add(mSubscription);
    }

    /**
     * 支付宝
     *
     * @param id
     * @param token
     */
    public void getAliAppPay(String id, String token) {

        mSubscription = RXUtil.execute(mService.getAliAppPay(id, token),
                new Observer<ResponseMessage<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<String> orderModelResponseMessage) {
                        if (orderModelResponseMessage.statusCode == 0) {
                            mView.showZfbDetails(orderModelResponseMessage.data);
                        } else {
                            mView.dataDefeated(orderModelResponseMessage.statusMessage);
                        }
                    }
                });
        mSubscriptions.add(mSubscription);
    }

    /**
     * 微信
     *
     * @param id
     * @param token
     */
    public void getWechatPay(String id, String token, String openid) {

        mSubscription = RXUtil.execute(mService.getWechatPay(id, token, "APP"),
                new Observer<ResponseMessage<OrderPayParam>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<OrderPayParam> orderModelResponseMessage) {
                        if (orderModelResponseMessage.statusCode == 0) {
                            mView.showWechatPayDetails(orderModelResponseMessage.data);
                        } else {
                            mView.dataDefeated(orderModelResponseMessage.statusMessage);
                        }
                    }
                });
        mSubscriptions.add(mSubscription);
    }
}
