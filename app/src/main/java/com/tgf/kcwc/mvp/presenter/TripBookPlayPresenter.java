package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TripBookModel;
import com.tgf.kcwc.mvp.model.TripBookService;
import com.tgf.kcwc.mvp.view.TripBookFindView;
import com.tgf.kcwc.mvp.view.TripBookPlayView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/5/4 0004
 * E-mail:hekescott@qq.com
 */

public class TripBookPlayPresenter extends WrapPresenter<TripBookPlayView> {

    private TripBookService mService;
    private TripBookPlayView mView;

    @Override
    public void attachView(TripBookPlayView view) {
        mView = view;
        mService = ServiceFactory.getTripBookService();

    }

    public void getTripBookList( Map<String, String> params) {
//    public void getTripBookList(String lat,@Query("lng") String lng
//            ,@Query("day") Integer day,@Query("start_lat") Double startLat,@Query("end_lat") Double endLat,@Query("end_lng") Double endLng) {


        RXUtil.execute(mService.getripBookPlayList(params), new Observer<ResponseMessage<TripBookModel>>() {
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
               if(tripBookModelResponseMessage.statusCode==0) {
                   mView.showTripBookList(tripBookModelResponseMessage.getData().list);
               }else {
                   CommonUtils.showToast(mView.getContext(),tripBookModelResponseMessage.statusMessage);
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
