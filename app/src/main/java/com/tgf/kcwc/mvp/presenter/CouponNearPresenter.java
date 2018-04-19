package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CouponCategory;
import com.tgf.kcwc.mvp.model.CouponDistanceFilterModel;
import com.tgf.kcwc.mvp.model.CouponNearModel;
import com.tgf.kcwc.mvp.model.CouponService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CouponNearView;
import com.tgf.kcwc.util.RXUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Query;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Created by susu on 17/1/22.
 */

public class CouponNearPresenter extends WrapPresenter<CouponNearView> {

    private  CouponNearView mView;
    private CouponService mService;
    @Override
    public void attachView(CouponNearView view) {
        mView = view;
        mService = ServiceFactory.getCouponService();
    }
    public void getNearList(int page, String latitude,String longitude, int distance,int categoryId,  int brandId,int order){
     Subscription subscription = RXUtil.execute(mService.getCouponNear(page, latitude, longitude, distance, categoryId, brandId, order), new Observer<ResponseMessage<CouponNearModel>>() {
         @Override
         public void onCompleted() {
             mView.setLoadingIndicator(false);
         }

         @Override
         public void onError(Throwable e) {

         }

         @Override
         public void onNext(ResponseMessage<CouponNearModel> couponNearModelResponseMessage) {
             CouponNearModel couponNearModel = couponNearModelResponseMessage.getData();
             mView.showNearList(couponNearModel);
         }
     }, new Action0() {
         @Override
         public void call() {
             mView.setLoadingIndicator(true);
         }
     });
        mSubscriptions.add(subscription);
    }
    /**
     * 获取排序分类
     */
    public void getRankOrder() {
        Subscription subsOrder = RXUtil.execute(mService.getCouponRankOrder(),
                new Observer<ResponseMessage<ArrayList<DataItem>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<ArrayList<DataItem>> arrayListResponseMessage) {
                        mView.showRankFilter(arrayListResponseMessage.getData());
                    }
                });
        mSubscriptions.add(subsOrder);
    }

    /**
     * 获取距离分类
     */
    public void getDistanceOrder() {
        RXUtil.execute(mService.getCouponDistanceFilter(),
                new Observer<ResponseMessage<CouponDistanceFilterModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<CouponDistanceFilterModel> couponDistanceFilterModelResponseMessage) {
                        mView.showDistanceOrderFilter( couponDistanceFilterModelResponseMessage.getData());
                    }
                });
    }
//    public void getAreaDatas(String parentId){
//        Subscription subsOrder =     RXUtil.execute(mService.getAreaDatas(parentId), new Observer<ResponseMessage<List<DataItem>>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(ResponseMessage<List<DataItem>> listResponseMessage) {
//                mView.showAreaList(listResponseMessage.getData());
//            }
//        });
//        mSubscriptions.add(subsOrder);
//    }
    public void  getRecomendCategory(){

        RXUtil.execute(mService.getCategoryList(), new Observer<ResponseMessage<ArrayList<CouponCategory>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showLoadingTasksError();
            }

            @Override
            public void onNext(ResponseMessage<ArrayList<CouponCategory>> arrayListResponseMessage) {
                mView.showCategorylist(arrayListResponseMessage.getData());
            }
        });

    }
}
