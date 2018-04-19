package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TripBookModel;
import com.tgf.kcwc.mvp.model.TripBookService;
import com.tgf.kcwc.mvp.view.TripBookFindView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/5/2 0002
 * E-mail:hekescott@qq.com
 */

public class TripBookCityPresneter extends WrapPresenter<TripBookFindView> {


    private TripBookFindView mView;
    private TripBookService mService;

    @Override
    public void attachView(TripBookFindView view) {
        mView = view;
        mService = ServiceFactory.getTripBookService();
    }

    public void getCityTripBookList(String lat, String lng, String adcode, int page) {
        RXUtil.execute(mService.getripBookCityList(lat, lng, adcode, page), new Observer<ResponseMessage<TripBookModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<TripBookModel> tripBookModelResponseMessage) {
                mView.showTripBookList(tripBookModelResponseMessage.getData().list);
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
}
