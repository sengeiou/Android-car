package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.MotoBuyService;
import com.tgf.kcwc.mvp.model.NewPriceModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.WaittingPriceModel;
import com.tgf.kcwc.mvp.view.WaittingPriceView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/2/22 0022
 * E-mail:hekescott@qq.com
 */

public class WaittingPricePresenter extends WrapPresenter<WaittingPriceView> {
    private WaittingPriceView mView;
    private MotoBuyService    mService;

    @Override
    public void attachView(WaittingPriceView view) {
        mView = view;
        mService = ServiceFactory.getMotoBuyService();
    }

    public void getWaittingPrice(String token, int carId, String lat, String log) {

        Subscription subscription = RXUtil.execute(mService.getWaittingPrice(token, carId, lat, log),
            new Observer<ResponseMessage<WaittingPriceModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ResponseMessage<WaittingPriceModel> waittingPriceModelResponseMessage) {
                    mView.showTitle(waittingPriceModelResponseMessage.data);
                    mView.showOrgList(waittingPriceModelResponseMessage.getData().org_list);

                }
            });
        mSubscriptions.add(subscription);
    }

    public void getNewPrice(String token, int orderId) {
        RXUtil.execute(mService.getNewPrice(token, orderId),
            new Observer<ResponseMessage<NewPriceModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ResponseMessage<NewPriceModel> newPriceModelResponseMessage) {
                    if (newPriceModelResponseMessage.data != null)
                        mView.showNewPrice(newPriceModelResponseMessage.getData());
                }
            });

    }
    public void cancelOrderApi(String token, int orderId) {
        RXUtil.execute(mService.deleteOrder(token, orderId), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                if (responseMessage.statusCode == 0) {

                    mView.cancelSuccess();
                } else {
                    mView.cancleFailed(responseMessage.statusMessage);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });

    }
}
