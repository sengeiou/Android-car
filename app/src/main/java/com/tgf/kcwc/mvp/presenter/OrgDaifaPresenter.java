package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.OrgDaifaModel;
import com.tgf.kcwc.mvp.model.OrgFenfaModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketManageService;
import com.tgf.kcwc.mvp.view.OrgDaifaView;
import com.tgf.kcwc.mvp.view.OrgFenfaView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;

/**
 * Auther: Scott
 * Date: 2017/10/18 0018
 * E-mail:hekescott@qq.com
 */

public class OrgDaifaPresenter extends WrapPresenter<OrgDaifaView> {
    private OrgDaifaView mView;
    private TicketManageService mService;

    @Override
    public void attachView(OrgDaifaView view) {
        mView = view;
        mService = ServiceFactory.getTicketManageService();
    }

    public void getTicketmOrgDaifaStatitics(String token, String eventId, String userName) {
        RXUtil.execute(mService.getTicketmOrgDaifaStatitics(token, eventId, userName), new Observer<ResponseMessage<OrgDaifaModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<OrgDaifaModel> orgFenfaModelResponseMessage) {
                if (orgFenfaModelResponseMessage.statusCode == 0) {
                    OrgDaifaModel fenfaModel = orgFenfaModelResponseMessage.getData();
                    mView.showFenfaStatitics(fenfaModel.list);
                } else {
                    CommonUtils.showToast(mView.getContext(), orgFenfaModelResponseMessage.statusMessage);
                }
            }
        });
    }
}
