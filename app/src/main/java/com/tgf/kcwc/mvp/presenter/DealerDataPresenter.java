package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ColorBean;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.StoreCarModel;
import com.tgf.kcwc.mvp.model.StoreDetailData;
import com.tgf.kcwc.mvp.model.StoreShowCarDetailModel;
import com.tgf.kcwc.mvp.model.VehicleService;
import com.tgf.kcwc.mvp.view.DealerDataView;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/22 16:55
 * E-mail：fishloveqin@gmail.com
 * 店铺Presenter
 */

public class DealerDataPresenter extends WrapPresenter<DealerDataView> {
    private DealerDataView mView;
    private Subscription mSubscription;
    private VehicleService mService = null;

    public void attachView(DealerDataView view) {
        this.mView = view;
        mService = ServiceFactory.getVehicleService();
    }

    /**
     * 获取店铺基本信息
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

                        mView.showData(repData.data);
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
     * 展车列表
     *
     * @param id
     */
    public void getShowCars(String id) {

        mSubscription = RXUtil.execute(mService.getShowCars(id),
                new Observer<ResponseMessage<StoreCarModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<StoreCarModel> repData) {

                        mView.showData(repData.data);
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
     * 现车列表
     *
     * @param id
     */
    public void getLiveCars(String id) {

        mSubscription = RXUtil.execute(mService.getLiveCars(id),
                new Observer<ResponseMessage<StoreCarModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<StoreCarModel> repData) {

                        mView.showData(repData.data);
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
     * 店内看列表
     *
     * @param brandId
     * @param cityId
     * @param page
     * @param priceKey
     * @param priceMin
     * @param priceMax
     * @param seatKey
     * @param longitude
     * @param latitude
     */
    public void getStoreCarLists(String brandId, String cityId, String carId, String page,
                                 String priceKey, String priceMin, String priceMax, String seatKey,
                                 String longitude, String latitude) {

        mSubscription = RXUtil
                .execute(
                        mService.getStoreCarList(brandId, cityId, carId, page, priceKey, priceMin, priceMax,
                                seatKey, longitude, latitude),
                        new Observer<ResponseMessage<StoreCarListModel>>() {
                            @Override
                            public void onCompleted() {
                                mView.setLoadingIndicator(false);
                            }

                            @Override
                            public void onError(Throwable e) {

                                mView.showLoadingTasksError();
                            }

                            @Override
                            public void onNext(ResponseMessage<StoreCarListModel> repData) {

                                mView.showData(repData.data);
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
     * 店内看车车系、车辆详情
     *
     * @param carId
     * @param orgId
     */
    public void getStoreCarDetail(String carId, String orgId, String token) {

        mSubscription = RXUtil.execute(mService.getStoreShowCarDetail(carId, orgId, token),
                new Observer<ResponseMessage<StoreShowCarDetailModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<StoreShowCarDetailModel> repData) {

                        mView.showData(repData.data);
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
     * 店内看车车系、车辆详情
     *
     * @param id
     */
    public void getStoreCarDetail(String id, String token) {

        mSubscription = RXUtil.execute(mService.getStoreShowCarDetail(id, token),
                new Observer<ResponseMessage<StoreShowCarDetailModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<StoreShowCarDetailModel> repData) {

                        mView.showData(repData.data);
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
     * 店内看车车系、车辆详情
     *
     * @param id
     */
    public void getColorsCfgLists(String id, String token) {

        mSubscription = RXUtil.execute(mService.getColorsCfgLists(id, token),
                new Observer<ResponseMessage<List<ColorBean>>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<ColorBean>> repData) {

                        mView.showData(repData.data);
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
