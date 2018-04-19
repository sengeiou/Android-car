package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.MotoBuyService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.YuyueBuyModel;
import com.tgf.kcwc.util.RXUtil;
import com.tgf.kcwc.view.YueyuBuyView;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/2/27 0027
 * E-mail:hekescott@qq.com
 */

public class YuYueServerPresenter extends WrapPresenter<YueyuBuyView> {
    public MotoBuyService mService;
    public YueyuBuyView mView;

    @Override
    public void attachView(YueyuBuyView view) {
        mView = view;
        mService = ServiceFactory.getMotoBuyService();

    }

    public  void  getCompeleteServerModel(String token, int carId){
       Subscription subscription = RXUtil.execute(mService.getYuyueBuy(token, carId), new Observer<ResponseMessage<YuyueBuyModel>>() {
           @Override
           public void onCompleted() {
               mView.setLoadingIndicator(false);
           }

           @Override
           public void onError(Throwable e) {

           }

           @Override
           public void onNext(ResponseMessage<YuyueBuyModel> completeServerModelResponseMessage) {
               mView.showHead(completeServerModelResponseMessage.getData());
           }
       }, new Action0() {
           @Override
           public void call() {
               mView.setLoadingIndicator(true);
           }
       });
        mSubscriptions.add(subscription);
    }

    public  void posCarorderCompelete(String token,int orderId){
        Subscription subscription = RXUtil.execute(mService.postCarOrderComplete(token, orderId), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                if (0==responseMessage.statusCode) {
                    mView.showCompleteSuccess();
                } else {
                    mView.showCompleteFailed();
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
        mSubscriptions.add(subscription);
    }

    public  void posClosePrice(String token,int orderId){
        Subscription subscription = RXUtil.execute(mService.postClosePrice(token, orderId), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                if (0==responseMessage.statusCode) {
                    mView.showCloseSuccess();
                } else {
                    mView.showCloseFailed(responseMessage.statusMessage);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
        mSubscriptions.add(subscription);
    }

}
