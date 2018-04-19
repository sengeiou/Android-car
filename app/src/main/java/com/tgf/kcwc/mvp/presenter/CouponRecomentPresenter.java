package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.CouponCategory;
import com.tgf.kcwc.mvp.model.CouponRecomentModel;
import com.tgf.kcwc.mvp.model.CouponService;
import com.tgf.kcwc.mvp.model.GeneralizationService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CouponRecomentView;
import com.tgf.kcwc.util.RXUtil;

import java.util.ArrayList;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/1/10 0010
 * E-mail:hekescott@qq.com
 */

public class CouponRecomentPresenter implements BasePresenter<CouponRecomentView> {
    private CouponService mService;
    private GeneralizationService mGenService;
private CouponRecomentView mView;
    @Override
    public void attachView(CouponRecomentView view) {
        mService = ServiceFactory.getCouponService();
        mGenService= ServiceFactory.getGeneralizationService();
        mView =view;
    }

    @Override
    public void detachView() {
    }

    public void getRecomend(String la,String lon){

        RXUtil.execute(mService.getRecomentList(la,lon), new Observer<ResponseMessage<CouponRecomentModel>>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                mView.showLoadingTasksError();
            }

            @Override
            public void onNext(ResponseMessage<CouponRecomentModel> couponRecomentModelResponseMessage) {
                CouponRecomentModel couponRecomentModel = couponRecomentModelResponseMessage.data;
                mView.showRecomendList(couponRecomentModel.likeCoupon);
                mView.showRecomendHead(couponRecomentModel.headRecomend);
            }
        });

    }
    public void  getRecomendCategory(){

        RXUtil.execute(mService.getCategoryList(), new Observer<ResponseMessage<ArrayList<CouponCategory>>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<ArrayList<CouponCategory>> arrayListResponseMessage) {
                mView.showCategorylist(arrayListResponseMessage.getData());
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });

    }
    public void getBannerView(String mark){
        RXUtil.execute(mGenService.getBanner("get", mark), new Observer<BannerModel>() {
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
