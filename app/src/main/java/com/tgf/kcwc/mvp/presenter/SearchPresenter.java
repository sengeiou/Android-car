package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.SearchModel;
import com.tgf.kcwc.mvp.model.SearchService;
import com.tgf.kcwc.mvp.view.SearchView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/8/18
 * @describle
 */
public class SearchPresenter extends WrapPresenter<SearchView>{
    Subscription mSubscription;
    SearchView mView;
    SearchService mService;
    @Override
    public void attachView(SearchView view) {
        mView = view;
        mService = ServiceFactory.getSearchService();
    }

    /**
     * 获取检索
     * @param token
     * @param friendId
     * @param time
     */
    public void getSearch(String token,int friendId,int time){
        mSubscription = RXUtil.execute(mService.getSearch(token, friendId, time), new Observer<ResponseMessage<SearchModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<SearchModel> searchModelResponseMessage) {
                if (searchModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS){
                    mView.showSearch(searchModelResponseMessage.data);
                }else {
                    CommonUtils.showToast(mView.getContext(),searchModelResponseMessage.statusMessage);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
        mSubscriptions.add(mSubscription);
    }
}
