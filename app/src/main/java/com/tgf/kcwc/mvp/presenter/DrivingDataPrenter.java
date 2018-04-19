package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.DrivingService;
import com.tgf.kcwc.mvp.model.DrvingListModel;
import com.tgf.kcwc.mvp.view.DrivDataView;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/18.
 */

public class DrivingDataPrenter extends WrapPresenter<DrivDataView> {

    private DrivDataView mView;
    private DrivingService mService;

    @Override
    public void attachView(DrivDataView view) {
        mView = view;
        mService = ServiceFactory.getDrivingService();
    }

    public void getListData(Map<String, String> params) {
        RXUtil.execute(mService.getDrivingList(params), new Observer<DrvingListModel>() {
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
            public void onNext(DrvingListModel drvingListModel) {
                if (drvingListModel.code == 0) {
                    mView.dataListSucceed(drvingListModel);
                } else {
                    mView.dataListDefeated(drvingListModel.msg);
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
        RXUtil.execute(mService.getDrivingBanner(token, "get", "CAR_THREAD_CYCLE"),
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
                        if (bannerModel.code == 0) {
                            mView.dataBannerSucceed(bannerModel);
                        } else {
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
