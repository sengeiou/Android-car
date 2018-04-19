package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TripBookService;
import com.tgf.kcwc.mvp.model.TripBookThreadModel;
import com.tgf.kcwc.mvp.view.TripbookThreadView;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;

import retrofit2.http.Query;
import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/5/15 0015
 * E-mail:hekescott@qq.com
 */

public class TripBookThreadPresenter extends WrapPresenter<TripbookThreadView> {


    private TripbookThreadView mView;
    private TripBookService mService;

    @Override
    public void attachView(TripbookThreadView view) {
        mView = view;
        mService = ServiceFactory.getTripBookService();
    }

    public void  getTripbookThreadList(int bookLineId,int page,int pageSize){
        RXUtil.execute(mService.getTripBookThread(bookLineId, page, pageSize), new Observer<ResponseMessage<TripBookThreadModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<TripBookThreadModel> tripBookThreadModelResponseMessage) {
                List<TripBookThreadModel.Thread> threadList = tripBookThreadModelResponseMessage.getData().threadList.list;
                mView.showThreadList(threadList);

            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
}
