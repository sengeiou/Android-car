package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.MotoBuyService;
import com.tgf.kcwc.mvp.model.MySeecarMsgModel;
import com.tgf.kcwc.mvp.model.NewPriceModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.MySeecarMsgView;
import com.tgf.kcwc.util.RXUtil;

import retrofit2.http.Query;
import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/4/18 0018
 * E-mail:hekescott@qq.com
 */

public class MySeecarMsgPresenter extends WrapPresenter<MySeecarMsgView> {
    private MotoBuyService  mService;
    private MySeecarMsgView mView;

    @Override
    public void attachView(MySeecarMsgView view) {
        mService = ServiceFactory.getMotoBuyService();
        mView = view;
    }

    public void getMySeecarMsgList(String token, int page, int pageSize, int type) {
        RXUtil.execute(mService.getMySeecarMsg(token, page, pageSize, type),
            new Observer<ResponseMessage<MySeecarMsgModel>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ResponseMessage<MySeecarMsgModel> newPriceModelResponseMessage) {
                    mView.showMySeecarList(newPriceModelResponseMessage.getData().orderList);
                    mView.showCount(newPriceModelResponseMessage.getData().pagination);
                }
            }, new Action0() {
                @Override
                public void call() {
                    mView.setLoadingIndicator(true);
                }
            });
    }

    public void postRemoveItem(String token, int id) {
        RXUtil.execute(mService.postRemoveOrder(token, id), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                    if(responseMessage.statusCode ==0){
                        mView.showDeleteSuccess();
                    }else {
                        mView.showDeleteFailed();
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
