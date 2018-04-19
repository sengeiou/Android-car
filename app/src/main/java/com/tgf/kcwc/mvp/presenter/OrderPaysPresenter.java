package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.OrderPayModel;
import com.tgf.kcwc.mvp.model.OrderPayService;
import com.tgf.kcwc.mvp.model.OrderProcessModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.OrderPaysView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/9/18
 * @describle
 */
public class OrderPaysPresenter extends WrapPresenter<OrderPaysView> {
    Subscription mSubscription;
    OrderPayService mService;
    OrderPaysView mView;
    @Override
    public void attachView(OrderPaysView view) {
        mView = view;
        mService = ServiceFactory.getOrderPayService();
    }

    /**
     * 获取订单详情
     * @param token
     * @param orderSn
     */
    public void getOrderDetail(String token,String orderSn){
        mSubscription = RXUtil.execute(mService.getOrderDetail(token, orderSn), new Observer<ResponseMessage<OrderPayModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<OrderPayModel> orderPayModelResponseMessage) {
                if (orderPayModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showOrderDetail(orderPayModelResponseMessage.data);
                } else {
                    CommonUtils.showToast(mView.getContext(), orderPayModelResponseMessage.statusMessage);
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
