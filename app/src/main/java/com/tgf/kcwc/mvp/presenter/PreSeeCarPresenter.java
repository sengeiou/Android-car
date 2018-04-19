package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.PreSeecarModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.SeecarService;
import com.tgf.kcwc.mvp.view.PreSeecarView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/3/9 0009
 * E-mail:hekescott@qq.com
 */

public class PreSeeCarPresenter extends WrapPresenter<PreSeecarView> {
    private PreSeecarView mView;
    private SeecarService mService;
    @Override
    public void attachView(PreSeecarView view) {
        mView= view;
        mService= ServiceFactory.getSeecarService();
    }
    public void getPreSeecarList(String token, int status, int page, int pageSize){
       Subscription subscription= RXUtil.execute(mService.getPreSeecarList(token, status, page, pageSize), new Observer<ResponseMessage<PreSeecarModel>>() {
           @Override
           public void onCompleted() {
               mView.setLoadingIndicator(false);
           }

           @Override
           public void onError(Throwable e) {
               mView.setLoadingIndicator(false);
           }

           @Override
           public void onNext(ResponseMessage<PreSeecarModel> preBuyMotoModelResponseMessage) {
               PreSeecarModel preSeecarModel=   preBuyMotoModelResponseMessage.getData();
               if(preBuyMotoModelResponseMessage.statusCode==0){
                   mView.showPreBuylist(preSeecarModel.list);
                   mView.showListCount(preSeecarModel.pagination);
               }else {
                   CommonUtils.showToast(mView.getContext(),preBuyMotoModelResponseMessage.statusMessage);

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
