package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.PreTicketModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketService;
import com.tgf.kcwc.mvp.view.PreRegTicketView;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.Subscription;

/**
 * Author：Jenny
 * Date:2016/12/14 15:55
 * E-mail：fishloveqin@gmail.com
 * 预报名
 */

public class PreRegTicketPresenter extends WrapPresenter<PreRegTicketView> {
    private PreRegTicketView mView;
    private Subscription     mSubscription;
    private TicketService    mService = null;

    @Override
    public void attachView(PreRegTicketView view) {
        this.mView = view;
        mService = ServiceFactory.getTicketService();
    }

    /**
     *
     * @param senseId
     * @param type 1发放 2申请
     * @param channelId 渠道3表示看车玩车
     * @param id
     * @param tid
     * @param token
     */
    public void getPreRegTicketInfos(String senseId, String type, String channelId, String id,
                                     String tid, String token) {

        mSubscription = RXUtil.execute(
            mService.preRegTicketList(senseId, type, channelId, id, tid, token),
            new Observer<ResponseMessage<PreTicketModel>>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(ResponseMessage<PreTicketModel> dataMsg) {

                    mView.showData(dataMsg.data);
                }
            });
        mSubscriptions.add(mSubscription);
    }

    public void getApplyStatus(String senseId, String qdId, String token) {

        mSubscription = RXUtil.execute(mService.applyStatus(senseId, qdId, token),
            new Observer<ResponseMessage<Object>>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(ResponseMessage<Object> dataMsg) {

                    DataItem d = new DataItem();
                    d.code = dataMsg.statusCode;
                    d.msg = dataMsg.statusMessage;
                    mView.showData(d);
                }
            });
        mSubscriptions.add(mSubscription);
    }

    public void commitForms(Map<String, String> paras) {

        mSubscription = RXUtil.execute(mService.createForms(paras),
            new Observer<ResponseMessage<List<String>>>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(ResponseMessage<List<String>> dataMsg) {

                    DataItem d = new DataItem();
                    d.code = dataMsg.statusCode;
                    d.msg = dataMsg.statusMessage;
                    mView.showData(d);
                }
            });
        mSubscriptions.add(mSubscription);
    }

}
