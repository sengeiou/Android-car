package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CompleteServerModel;
import com.tgf.kcwc.mvp.model.MotoBuyService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.YuyueBuyModel;
import com.tgf.kcwc.util.RXUtil;
import com.tgf.kcwc.view.CompleteServerView;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/2/27 0027
 * E-mail:hekescott@qq.com
 */

public class CompleteServerPresenter extends WrapPresenter<CompleteServerView> {
    public MotoBuyService mService;
    public CompleteServerView mView;

    @Override
    public void attachView(CompleteServerView view) {
        mView = view;
        mService = ServiceFactory.getMotoBuyService();

    }

    public  void  getCompeleteServerModel(String token, int orderId){
       Subscription subscription = RXUtil.execute(mService.getCompleteServer(token, orderId), new Observer<ResponseMessage<YuyueBuyModel>>() {
           @Override
           public void onCompleted() {
               mView.setLoadingIndicator(false);
           }

           @Override
           public void onError(Throwable e) {
               mView.setLoadingIndicator(false);
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


}
