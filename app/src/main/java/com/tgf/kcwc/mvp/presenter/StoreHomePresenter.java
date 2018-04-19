package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.StoreDetailData;
import com.tgf.kcwc.mvp.model.VehicleService;
import com.tgf.kcwc.mvp.view.StoreHomeView;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/22 16:55
 * E-mail：fishloveqin@gmail.com
 * 店铺主页Presenter
 */

public class StoreHomePresenter extends WrapPresenter<StoreHomeView> {
    private StoreHomeView mView;
    private Subscription mSubscription;
    private VehicleService mService = null;

    public void attachView(StoreHomeView view) {
        this.mView = view;
        mService = ServiceFactory.getVehicleService();
    }

    /**
     * 获取代金券列表
     *
     * @param orgId
     */
    public void loadCouponList(String orgId) {

        mSubscription = RXUtil.execute(mService.couponList(orgId),
                new Observer<ResponseMessage<List<Coupon>>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<Coupon>> couponListDataResponseMessage) {

                        mView.showCouponList(couponListDataResponseMessage.data);
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
     * 获取店铺信息
     *
     * @param id
     */
    public void loadStoreInfo(String id, String token) {

        mSubscription = RXUtil.execute(mService.storeDetail(id, token),
                new Observer<ResponseMessage<StoreDetailData>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<StoreDetailData> repData) {

                        mView.showStoreInfo(repData.data);
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
     * 礼包列表
     *
     * @param orgId
     */
    public void loadGiftList(String orgId, final String type, String token) {

        mSubscription = RXUtil.execute(mService.giftsList(orgId, type, token),
                new Observer<ResponseMessage<List<Coupon>>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<Coupon>> responseMessage) {

                        if (type.equals("1")) {
                            mView.showPrivileges(responseMessage.data);
                        } else if (type.equals("2")) {
                            mView.showGifts(responseMessage.data);
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
