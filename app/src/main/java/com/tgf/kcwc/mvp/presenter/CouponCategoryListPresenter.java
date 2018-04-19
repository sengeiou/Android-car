package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.CouponCategory;
import com.tgf.kcwc.mvp.model.CouponCategoryListModel;
import com.tgf.kcwc.mvp.model.CouponDistanceFilterModel;
import com.tgf.kcwc.mvp.model.CouponService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CouponCategoryListView;
import com.tgf.kcwc.util.RXUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/1/24 0024
 * E-mail:hekescott@qq.com
 */

public class CouponCategoryListPresenter extends WrapPresenter<CouponCategoryListView> {
    private CouponCategoryListView mView;
    private CouponService          mService;

    @Override
    public void attachView(CouponCategoryListView view) {
        mView = view;
        mService = ServiceFactory.getCouponService();
    }

    public void getCouponlist(int categoryId,int brandId,int cityId,int distanceId,int orderId, String latitude,String longitude, int page) {
        Subscription subs = RXUtil.execute(mService.getCouponCategorylist(categoryId, brandId, cityId, distanceId, orderId, latitude, longitude, page),
                new Observer<ResponseMessage<CouponCategoryListModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setLoadingIndicator(false);
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<CouponCategoryListModel> couponCategoryListModelResponseMessage) {
                        ArrayList<Coupon> couponList = couponCategoryListModelResponseMessage
                                .getData().couponArrayList;
                        mView.showCouponList(couponList);
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                });
        mSubscriptions.add(subs);
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
        Subscription subsOrder =   RXUtil.execute(mService.getCouponDistanceFilter(),
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
                    mView.showDistanceOrderFilter(
                        couponDistanceFilterModelResponseMessage.getData());
                }
            });
        mSubscriptions.add(subsOrder);
    }
    public void  getRecomendCategory(){

        Subscription subsOrder =     RXUtil.execute(mService.getCategoryList(), new Observer<ResponseMessage<ArrayList<CouponCategory>>>() {
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
        mSubscriptions.add(subsOrder);
    }
    public void getAreaDatas(String parentId){
        Subscription subsOrder =     RXUtil.execute(mService.getAreaDatas(parentId), new Observer<ResponseMessage<List<DataItem>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<List<DataItem>> listResponseMessage) {
                mView.showAreaList(listResponseMessage.getData());
            }
        });
        mSubscriptions.add(subsOrder);
    }


}
