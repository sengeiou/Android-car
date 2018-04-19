package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.NearAttractionModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TripBookService;
import com.tgf.kcwc.mvp.view.NearAttrationView;
import com.tgf.kcwc.util.RXUtil;

import retrofit2.http.Query;
import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/5/10 0010
 * E-mail:hekescott@qq.com
 */

public class NearAttrationPresenter extends WrapPresenter<NearAttrationView> {

    private NearAttrationView mView;
    private TripBookService   mService;

    @Override
    public void attachView(NearAttrationView view) {
        mView = view;
        mService = ServiceFactory.getTripBookService();
    }
    public void getNearAttraionlist(String token,  int type, String lat
            ,  String lng, Integer page,Integer pageSize){
        RXUtil.execute(mService.getNearAttraction(token, type, lat, lng, page, pageSize), new Observer<ResponseMessage<NearAttractionModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<NearAttractionModel> nearAttractionModelResponseMessage) {
                mView.showNearAttrationlist(nearAttractionModelResponseMessage.getData().list);
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
}
