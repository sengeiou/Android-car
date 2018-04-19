package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.OrgGailanModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketManageService;
import com.tgf.kcwc.mvp.view.OrgGailanView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/10/17 0017
 * E-mail:hekescott@qq.com
 */

public class TicketmGailanPresenter extends WrapPresenter<OrgGailanView> {

    private TicketManageService mService;
    private OrgGailanView mView;

    @Override
    public void attachView(OrgGailanView view) {
        mService = ServiceFactory.getTicketManageService();
        mView = view;
    }
    public void getTicketmGailan(String token, String eventId){
     Subscription subscription= RXUtil.execute(mService.getTicketmOrgGailanTongji(token, eventId), new Observer<ResponseMessage<OrgGailanModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<OrgGailanModel> orgGailanModelResponseMessage) {
                    if(orgGailanModelResponseMessage.statusCode==0){
                        OrgGailanModel  model =  orgGailanModelResponseMessage.getData();
                        mView.showOrgGailanTongji(model.handTongjilist);
                        mView.showHead(model.ticketmExhibitModel);
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
