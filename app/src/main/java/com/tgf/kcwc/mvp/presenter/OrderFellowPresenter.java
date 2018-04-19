package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.MotoBuyService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.YuyueBuyModel;
import com.tgf.kcwc.mvp.view.OrderFellowView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/3/7 0007
 * E-mail:hekescott@qq.com
 */

public class OrderFellowPresenter extends WrapPresenter<OrderFellowView> {
    private OrderFellowView mView;
    private MotoBuyService mService;
    @Override
    public void attachView(OrderFellowView view) {
        mView = view;
        mService= ServiceFactory.getMotoBuyService();
    }
    public void getOrderFellowContact(String token,int id){
        RXUtil.execute(mService.getYuyueBuy(token, id), new Observer<ResponseMessage<YuyueBuyModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<YuyueBuyModel> yuyueBuyModelResponseMessage) {
            YuyueBuyModel yuyueBuyModel =    yuyueBuyModelResponseMessage.getData();
                    mView.showOrgList(yuyueBuyModel.offer_list);
            }
        });
    }
    public void postNodisturb(String token,int id,int offer_id){
        RXUtil.execute(mService.postNodisturb(token, id, offer_id), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
                mView.showPostDataFailed(e.getMessage());
            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                if (responseMessage.statusCode == 0) {
                    mView.showNoDisturbSuccess("消息免打扰成功");
                } else {
                    mView.showPostDataFailed(responseMessage.statusMessage);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
    public void postOrderFellowComplete(String token,int id,int offer_id){
        RXUtil.execute(mService.postOrderfellowComplete(token, id, offer_id), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
                mView.showPostDataFailed(e.getMessage());
            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                if (responseMessage.statusCode == 0) {
                    mView.showCompleteSuccess("完成订单服务");
                } else {
                    mView.showPostDataFailed(responseMessage.statusMessage);
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


