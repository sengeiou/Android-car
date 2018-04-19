package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.ExhibitionService;
import com.tgf.kcwc.mvp.model.HomeModel;
import com.tgf.kcwc.mvp.model.MotoOrder;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.HomePageMoreView;
import com.tgf.kcwc.mvp.view.HomePageView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/5/15 0015
 * E-mail:hekescott@qq.com
 */

public  class HomePageMorePresenter extends WrapPresenter<HomePageMoreView> {


    private HomePageMoreView mView;
    private ExhibitionService mService;

    @Override
    public void attachView(HomePageMoreView view) {
        mView = view;
        mService = ServiceFactory.getExhibitionService();
    }

    public void getHomePageView(String mark){
        RXUtil.execute(mService.getBanner("get", mark), new Observer<BannerModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BannerModel bannerModel) {
                    mView.showBannerView(bannerModel.data);
            }
        });
    }


}
