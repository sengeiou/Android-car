package com.tgf.kcwc.mvp.presenter;

import android.util.Log;

import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.OrderProcessModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.RobOrderDecryptService;
import com.tgf.kcwc.mvp.view.RobOrderDecryptView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/7/31 11:22
 */

public class RobOrderDecryptPresenter extends WrapPresenter<RobOrderDecryptView> {
    RobOrderDecryptView mDecryptView;
    RobOrderDecryptService mDecryptService;

    @Override
    public void attachView(RobOrderDecryptView view) {
        mDecryptView = view;
        mDecryptService = ServiceFactory.getOrderProcessService();
    }

    public void getOrderProcessList(String token,String userId,int page) {
        RXUtil.execute(mDecryptService.getOrderProcess(token,userId,page), new Observer<ResponseMessage<OrderProcessModel>>() {
            @Override
            public void onCompleted() {
                mDecryptView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "onError: "+e.getMessage().toString());
                mDecryptView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<OrderProcessModel> orderProcessItemResponseMessage) {
                mDecryptView.showOrderProcess(orderProcessItemResponseMessage.data.list);
                mDecryptView.showUserData(orderProcessItemResponseMessage.data.userItem);
            }
        }, new Action0() {
            @Override
            public void call() {
                mDecryptView.setLoadingIndicator(true);
            }
        });
    }
}
