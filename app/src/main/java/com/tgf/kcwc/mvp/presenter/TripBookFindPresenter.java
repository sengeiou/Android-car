package com.tgf.kcwc.mvp.presenter;

import com.amap.api.services.core.LatLonPoint;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TripBookModel;
import com.tgf.kcwc.mvp.model.TripBookService;
import com.tgf.kcwc.mvp.view.TripBookFindView;
import com.tgf.kcwc.util.RXUtil;

import retrofit2.http.Query;
import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/5/4 0004
 * E-mail:hekescott@qq.com
 */

public class TripBookFindPresenter extends WrapPresenter<TripBookFindView> {

    private TripBookService mService;
    private TripBookFindView mView;

    @Override
    public void attachView(TripBookFindView view) {
        mView = view;
        mService = ServiceFactory.getTripBookService();

    }

    public void getTripBookList(String lat, String lng
            , Integer day, String startPoint, String endPoint,int page) {
//    public void getTripBookList(String lat,@Query("lng") String lng
//            ,@Query("day") Integer day,@Query("start_lat") Double startLat,@Query("end_lat") Double endLat,@Query("end_lng") Double endLng) {


        RXUtil.execute(mService.getripBookFindList(lat, lng, day, startPoint, endPoint,page), new Observer<ResponseMessage<TripBookModel>>() {
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
