package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TripAroundTongxModel;
import com.tgf.kcwc.mvp.model.TripBookService;
import com.tgf.kcwc.mvp.view.TripAroundTongXView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/5/16 0016
 * E-mail:hekescott@qq.com
 */

public class TripAroundTongxPresenter extends WrapPresenter<TripAroundTongXView> {

    private TripAroundTongXView mView;
    private TripBookService mService;

    @Override
    public void attachView(TripAroundTongXView view) {
        mView = view;
        mService = ServiceFactory.getTripBookService();
    }
    public void getAroudTongxlist(int bookLineId){
        RXUtil.execute(mService.getTripAroudTongX(bookLineId), new Observer<ResponseMessage<TripAroundTongxModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<TripAroundTongxModel> tripAroundOrgModelResponseMessage) {
                mView.showTongXlist(tripAroundOrgModelResponseMessage.getData().modelList.tongxList);
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
}
