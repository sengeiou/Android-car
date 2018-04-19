package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.PleasePlayModel;
import com.tgf.kcwc.mvp.model.PleaseService;
import com.tgf.kcwc.mvp.view.PleaseDataView;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/18.
 */

public class PleaseDataPrenter extends WrapPresenter<PleaseDataView> {

    private PleaseDataView   mView;
    private PleaseService mService;

    @Override
    public void attachView(PleaseDataView view) {
        mView = view;
        mService = ServiceFactory.getPleaseService();
    }

    public void getListData(Map<String, String> params) {
        RXUtil.execute(mService.getPleaseList(params), new Observer<PleasePlayModel>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(PleasePlayModel pleasePlayModel) {
                if (pleasePlayModel.code==0){
                    mView.dataListSucceed(pleasePlayModel);
                }else {
                    mView.dataListDefeated(pleasePlayModel.msg);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void getBannerData(String token) {
        RXUtil.execute(mService.getPleaseBanner(token, "get", "CAR_THREAD_PLAY"),
            new Observer<BannerModel>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {
                    mView.dataBannerDefeated("网络异常，稍候再试！");
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onNext(BannerModel bannerModel) {
                    if (bannerModel.code==0){
                        mView.dataBannerSucceed(bannerModel);
                    }else {
                        mView.dataListDefeated(bannerModel.msg);
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
