package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.RapBuymotoModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.SeecarService;
import com.tgf.kcwc.mvp.view.RapOrderMotoView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/3/9 0009
 * E-mail:hekescott@qq.com
 */

public class RapSeeCarPresenter extends WrapPresenter<RapOrderMotoView>{
    private SeecarService mService;
    private RapOrderMotoView mView;
    @Override
    public void attachView(RapOrderMotoView view) {
        mService = ServiceFactory.getSeecarService();
        mView =view;
    }

    public void getRapSeecarlist(String token, int page, int pageSize,int type){
        RXUtil.execute(mService.getRapOrdermotoList(token, page, pageSize,type), new Observer<ResponseMessage<RapBuymotoModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<RapBuymotoModel> preBuymotoModelResponseMessage) {
                mView.showPreBuyList(preBuymotoModelResponseMessage.getData().preBuymotolist);
                mView.showHead(preBuymotoModelResponseMessage.getData().pagination);
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
}
