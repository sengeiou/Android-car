package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.EaseLoginModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.UserManagerService;
import com.tgf.kcwc.mvp.view.EaseLoginView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/8/1 0001
 * E-mail:hekescott@qq.com
 */

public class EaseLoginPresenter extends WrapPresenter<EaseLoginView> {
    private UserManagerService mService;
    private EaseLoginView mView;
    private Subscription subscription;

    @Override
    public void attachView(EaseLoginView view) {
        mView = view;
        mService = ServiceFactory.getUMService();
    }
    public void getEaseLogin(String token,String type){

        subscription = RXUtil.execute(mService.getLoginEase(token,type), new Observer<ResponseMessage<EaseLoginModel>>() {
              @Override
              public void onCompleted() {

              }

              @Override
              public void onError(Throwable e) {

              }

              @Override
              public void onNext(ResponseMessage<EaseLoginModel> easeLoginModelResponseMessage) {

                      if(easeLoginModelResponseMessage.statusCode==0){
                          mView.showEaseLoginSuccess(easeLoginModelResponseMessage.getData());
                      }
              }
          });

    }

    @Override
    public void detachView() {
        super.detachView();
        if(subscription!=null){
            if(subscription.isUnsubscribed()){
                subscription.unsubscribe();
            }
        }
    }
}
