package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketManageService;
import com.tgf.kcwc.mvp.model.TicketOrgManageDetailModel;
import com.tgf.kcwc.mvp.view.TicketOrgManageDetailView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import retrofit2.http.Query;
import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/10/19 0019
 * E-mail:hekescott@qq.com
 */

public class TicketOrgManageDetailPresenter extends WrapPresenter<TicketOrgManageDetailView> {

    private  TicketOrgManageDetailView mView;
    private TicketManageService mService;
    @Override
    public void attachView(TicketOrgManageDetailView view) {
        mView =view;
        mService = ServiceFactory.getTicketManageService();
    }

    public void getTicketOrgManageDetail(String token, int id, int isTongji){
       Subscription subscription =   RXUtil.execute(mService.getTicketOrgManageDetail(token, id, isTongji), new Observer<ResponseMessage<TicketOrgManageDetailModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<TicketOrgManageDetailModel> ticketOrgManageDetailModelResponseMessage) {
                if(ticketOrgManageDetailModelResponseMessage.statusCode==0){

                    mView.showManageDetail(ticketOrgManageDetailModelResponseMessage.getData().details);
                }else {
                    CommonUtils.showToast(mView.getContext(),ticketOrgManageDetailModelResponseMessage.statusMessage);
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
