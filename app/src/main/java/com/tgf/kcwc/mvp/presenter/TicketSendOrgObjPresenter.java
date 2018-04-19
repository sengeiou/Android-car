package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketManageService;
import com.tgf.kcwc.mvp.model.TicketSendOrgObjModel;
import com.tgf.kcwc.mvp.view.TicketSendOrgObjView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;

/**
 * Auther: Scott
 * Date: 2017/10/23 0023
 * E-mail:hekescott@qq.com
 */

public class TicketSendOrgObjPresenter extends WrapPresenter<TicketSendOrgObjView> {
    private TicketSendOrgObjView mView;
    private TicketManageService mService;

    @Override
    public void attachView(TicketSendOrgObjView view) {
        mView = view;
        mService = ServiceFactory.getTicketManageService();
    }
    public void getTicketSendOrgObj(String token, int id){
        RXUtil.execute(mService.getTicketSendOrgObj(token, id), new Observer<ResponseMessage<TicketSendOrgObjModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<TicketSendOrgObjModel> ticketSendOrgObjModelResponseMessage) {
                    if(ticketSendOrgObjModelResponseMessage.statusCode==0){
                        mView.showSendObjData(ticketSendOrgObjModelResponseMessage.getData());
                    }else {
                        CommonUtils.showToast(mView.getContext(),ticketSendOrgObjModelResponseMessage.statusMessage);
                    }
            }
        });
    }
}
