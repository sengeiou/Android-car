package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BeautyDetailModel;
import com.tgf.kcwc.mvp.model.ExhibitionService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.BeautyDetailView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/1/17 0017
 * E-mail:hekescott@qq.com
 */

public class BeautyDetailPresenter extends WrapPresenter<BeautyDetailView> {
    private BeautyDetailView  mView;
    private ExhibitionService mService;

    @Override
    public void attachView(BeautyDetailView view) {
        mView = view;
        mService = ServiceFactory.getExhibitionService();
    }

    public void getBeautyDetail(int beautyId, String token, int pageSize, int page) {
        Subscription subscription = RXUtil.execute(
                mService.getBeautyDetail(beautyId, token, pageSize, page),
                new Observer<ResponseMessage<BeautyDetailModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<BeautyDetailModel> beautyDetailModelResponseMessage) {
                        BeautyDetailModel beautyDetailModel = beautyDetailModelResponseMessage.getData();
                        mView.showHead(beautyDetailModel);
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
