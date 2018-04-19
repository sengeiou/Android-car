package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.TripBookModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TripBookService;
import com.tgf.kcwc.mvp.view.MyTripBookView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/4/28 0028
 * E-mail:hekescott@qq.com
 */

public class MyTripbookPresenter extends WrapPresenter<MyTripBookView> {

    private MyTripBookView  mView;
    private TripBookService mService;

    @Override
    public void attachView(MyTripBookView view) {
        mView = view;
        mService = ServiceFactory.getTripBookService();
    }

    public void getMyTripBooklist(String token) {
        Subscription subscription =  RXUtil.execute(mService.getMyTripBookList(token),
            new Observer<ResponseMessage<TripBookModel>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onNext(ResponseMessage<TripBookModel> myTripBookModelResponseMessage) {
                    mView.showMyBooklist(myTripBookModelResponseMessage.getData().list);
                }
            }, new Action0() {
                @Override
                public void call() {
                    mView.setLoadingIndicator(true);
                }
            });
        mSubscriptions.add(subscription);
    }
}
