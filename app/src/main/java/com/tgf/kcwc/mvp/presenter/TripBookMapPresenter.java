package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TripBookMapModel;
import com.tgf.kcwc.mvp.model.TripBookService;
import com.tgf.kcwc.mvp.view.TripMapView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/5/17 0017
 * E-mail:hekescott@qq.com
 */

public class TripBookMapPresenter extends WrapPresenter<TripMapView> {


    private TripMapView mapViewiew;
    private TripBookService mService;

    @Override
    public void attachView(TripMapView view) {
        mapViewiew = view;
        mService = ServiceFactory.getTripBookService();
    }
    public void getTripmapDetail(int bookId,String lat,String lng){
        RXUtil.execute(mService.getTripMapdetail(bookId, lat, lng), new Observer<ResponseMessage<TripBookMapModel>>() {
            @Override
            public void onCompleted() {
                mapViewiew.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mapViewiew.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<TripBookMapModel> tripBookMapModelResponseMessage) {
                TripBookMapModel mTripBookMapModel=   tripBookMapModelResponseMessage.getData();
                mapViewiew.showTripMapdetail(mTripBookMapModel.mapItemList);
                mapViewiew.showTripNodeList(mTripBookMapModel.nodeList);
            }
        }, new Action0() {
            @Override
            public void call() {
                mapViewiew.setLoadingIndicator(true);
            }
        });

    }
}
