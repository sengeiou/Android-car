package com.tgf.kcwc.mvp.presenter;

import com.google.gson.Gson;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.StoreBrand;
import com.tgf.kcwc.mvp.model.VehicleService;
import com.tgf.kcwc.mvp.view.BrandDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;
import com.tgf.kcwc.util.SharedPreferenceUtil;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/14 15:55
 * E-mail：fishloveqin@gmail.com
 * 整车-品牌列表Presenter
 */

public class BrandListPresenter extends WrapPresenter<BrandDataView> {
    private BrandDataView mView;
    private Subscription mSubscription;
    private VehicleService mService = null;

    @Override
    public void attachView(BrandDataView view) {
        this.mView = view;
        mService = ServiceFactory.getVehicleService();
    }

    /**
     * 获取品牌列表
     */
    public void loadBrandsDatas(String type, String isNeedAll) {

        mSubscription = RXUtil.execute(mService.brandList(type, isNeedAll),
                new Observer<ResponseMessage<List<Brand>>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<Brand>> responseMessage) {

                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            List<Brand> data = responseMessage.data;
                            mView.showData(data);
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
     * 获取品牌列表
     */
    public void loadBrandsDatas(String type) {

        mSubscription = RXUtil.execute(mService.brandList(type),
                new Observer<ResponseMessage<List<Brand>>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<Brand>> responseMessage) {

                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            List<Brand> data = responseMessage.data;
                            Gson gson = new Gson();
                            gson.toJson(data);
                            SharedPreferenceUtil.putBrandlist(mView.getContext(), gson.toJson(data));
                            mView.showData(data);
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
     * 获取拍店内展车品牌列表
     */
    public void getaddbrandList(String token) {

        mSubscription = RXUtil.execute(mService.getaddbrandList(token),
                new Observer<ResponseMessage<StoreBrand>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<StoreBrand> responseMessage) {

                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            List<Brand> data = responseMessage.data.dataList;
                            mView.showData(data);
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
     * 获取拍展会展车品牌列表
     */
    public void getExhibitionaddbrandList(String token,String event_id) {

        mSubscription = RXUtil.execute(mService.getExhibitionaddbrandList(token,event_id),
                new Observer<ResponseMessage<StoreBrand>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<StoreBrand> responseMessage) {

                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            List<Brand> data = responseMessage.data.dataList;
                            mView.showData(data);
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
     * 参展品牌
     */
    public void loadExhibitionBrandsDatas(String eventId, String isNeedAll) {

        mSubscription = RXUtil.execute(mService.getExhibitionBrandList(eventId, isNeedAll),
                new Observer<ResponseMessage<List<Brand>>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<Brand>> responseMessage) {

                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            List<Brand> data = responseMessage.data;
                            mView.showData(data);
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
     * 参展品牌
     */
    public void loadSepBrandsDatas(String eventId, String isNeedAll) {

        mSubscription = RXUtil.execute(mService.sepBrandList(eventId, isNeedAll),
                new Observer<ResponseMessage<List<Brand>>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<Brand>> responseMessage) {

                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            List<Brand> data = responseMessage.data;
                            mView.showData(data);
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
     * 获取一级品牌列表
     */
    public void getBrandsDatas(String token, String includeAll) {

        mSubscription = RXUtil.execute(mService.getBrandsLists(token, includeAll),
                new Observer<ResponseMessage<List<Brand>>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<Brand>> responseMessage) {

                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            List<Brand> data = responseMessage.data;
                            mView.showData(data);
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
