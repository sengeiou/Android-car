package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketManageService;
import com.tgf.kcwc.mvp.model.TicketmExhibitModel;
import com.tgf.kcwc.mvp.model.TicketmListModel;
import com.tgf.kcwc.mvp.view.TicketManagerListView;
import com.tgf.kcwc.util.RXUtil;

import java.util.ArrayList;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/10/16 0016
 * E-mail:hekescott@qq.com
 */

public class TicketManagerListPresenter extends WrapPresenter<TicketManagerListView> {
    TicketManageService mService;
    private TicketManagerListView mView;

    @Override
    public void attachView(TicketManagerListView view) {
        mService = ServiceFactory.getTicketManageService();
        mView = view;
    }
    public void getTicketmExhibitList(String token){
        RXUtil.execute(mService.getTicketmExhibitlist(token), new Observer<ResponseMessage<ArrayList<TicketmExhibitModel>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<ArrayList<TicketmExhibitModel>> ticketmExhibitListModelResponseMessage) {
                if(ticketmExhibitListModelResponseMessage.statusCode==0){
                    mView.showExhibitList(ticketmExhibitListModelResponseMessage.getData());
                }
            }
        });
    }
    public void getTicketmList(String token,String eventId){
        RXUtil.execute(mService.getTicketmlist(token, eventId), new Observer<ResponseMessage<TicketmListModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<TicketmListModel> ticketmListModelResponseMessage) {
                if (ticketmListModelResponseMessage.statusCode == 0) {
                    TicketmListModel model = ticketmListModelResponseMessage.getData();
                    mView.showHandTicketList(model.handleList);
                    mView.showHead(model.eventInfo,model.nums);
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
