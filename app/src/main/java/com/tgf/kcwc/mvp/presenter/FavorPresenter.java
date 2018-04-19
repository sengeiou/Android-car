package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.GeneralizationService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.FavoriteView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/7/24 0024
 * E-mail:hekescott@qq.com
 */

public class FavorPresenter extends WrapPresenter<FavoriteView> {
    private FavoriteView mView;
    private GeneralizationService mService;
    @Override
    public void attachView(FavoriteView view) {
        mView = view;
        mService = ServiceFactory.getGeneralizationService();
    }
    /**
     * 添加收藏
     * @param params
     */
    public void addFavoriteData(Map<String, String> params) {

        Subscription mSubscription = RXUtil.execute(mService.addFavoriteData(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.addFavoriteSuccess(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
    /**
     * 取消收藏
     * @param params
     */
    public void cancelFavoriteData(Map<String, String> params) {

        Subscription mSubscription  = RXUtil.execute(mService.cancelFavoriteData(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.cancelFavorite(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
