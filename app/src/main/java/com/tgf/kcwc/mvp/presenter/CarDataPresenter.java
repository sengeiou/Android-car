package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.mvp.model.CarColor;
import com.tgf.kcwc.mvp.model.FactorySeriesModel;
import com.tgf.kcwc.mvp.model.GalleryDetailModel;
import com.tgf.kcwc.mvp.model.ModelGalleryModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.SeriesDetailModel;
import com.tgf.kcwc.mvp.model.SeriesModel;
import com.tgf.kcwc.mvp.model.TopicTagDataModel;
import com.tgf.kcwc.mvp.model.VehicleService;
import com.tgf.kcwc.mvp.view.CarDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/8 20:55
 * E-mail：fishloveqin@gmail.com
 * 整车-车型列表Presenter
 */

public class CarDataPresenter extends WrapPresenter<CarDataView> {
    private CarDataView mView;
    private Subscription mSubscription;
    private VehicleService mService = null;

    @Override
    public void attachView(CarDataView view) {
        this.mView = view;
        mService = ServiceFactory.getVehicleService();
    }

    /**
     * 获取车列表
     *
     * @param brandId
     * @param page
     * @param priceKey
     * @param priceMin
     * @param priceMax
     * @param seatKey
     */
    public void loadDatas(String brandId, String page, String priceKey, String priceMin,
                          String priceMax, String seatKey, String isFirst, String token) {

        mSubscription = RXUtil.execute(mService.getSeriesList(brandId, page, priceKey, priceMin,
                priceMax, seatKey, isFirst, token), new Observer<ResponseMessage<SeriesModel>>() {
            @Override
            public void onCompleted() {

                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.showLoadingTasksError();
            }

            @Override
            public void onNext(ResponseMessage<SeriesModel> motoListDataResponseMessage) {
                mView.showData(motoListDataResponseMessage.data);
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
     * 获取车列表
     *
     * @param params
     */
    public void loadDatas(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.getSeriesList(params), new Observer<ResponseMessage<SeriesModel>>() {
            @Override
            public void onCompleted() {

                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.showLoadingTasksError();
            }

            @Override
            public void onNext(ResponseMessage<SeriesModel> motoListDataResponseMessage) {
                mView.showData(motoListDataResponseMessage.data);
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
     * 根据品牌id获取车型列表
     *
     * @param brandId
     */
    public void loadDatas(String brandId, String token) {

        mSubscription = RXUtil.execute(mService.getCarsByBrandId(brandId, token),
                new Observer<ResponseMessage<List<CarBean>>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<CarBean>> motoListDataResponseMessage) {
                        // mView.showModelDatas(motoListDataResponseMessage.data);
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
     * 获取车型详情
     *
     * @param seriesId
     */
    public void getSeriesDetail(String seriesId, String token) {

        mSubscription = RXUtil.execute(mService.getSeriesDetail(seriesId, token),
                new Observer<ResponseMessage<SeriesDetailModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<SeriesDetailModel> responseMessage) {
                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            mView.showData(responseMessage.data);
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
     * 获取车系、车型图库
     *
     * @param seriesId
     * @param type     car  or series
     * @param outId
     * @param inId
     * @param carId
     */
    public void getCarGallery(String seriesId, String type, String outId, String inId,
                              String carId) {

        mSubscription = RXUtil.execute(mService.getCarGallery(seriesId, type, outId, inId, carId),
                new Observer<ResponseMessage<ModelGalleryModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<ModelGalleryModel> responseMessage) {
                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            mView.showData(responseMessage.data);
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

    public void getCarCategoryColors(String id, String type, String colorType) {

        mSubscription = RXUtil.execute(mService.getCarCategoryColors(id, type, colorType),
                new Observer<ResponseMessage<List<CarColor>>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<CarColor>> responseMessage) {
                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            mView.showData(responseMessage.data);
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
     * 根据品牌id获取车系列表
     *
     * @param brandId
     */
    public void getSeriesByBrand(String brandId, String token) {

        mSubscription = RXUtil.execute(mService.getSeriesByBrand(brandId, token),
                new Observer<ResponseMessage<List<CarBean>>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<CarBean>> responseMessage) {
                        mView.showData(responseMessage.data);
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
     * 根据车系id获取车型列表
     *
     * @param seriesId
     */
    public void getCarBySeries(String seriesId, String token) {

        mSubscription = RXUtil.execute(mService.getCarBySeries(seriesId, token),
                new Observer<ResponseMessage<List<CarBean>>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<CarBean>> responseMessage) {
                        mView.showData(responseMessage.data);
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
     * 车系、车型图库详情
     *
     * @param
     */
    public void getGalleryDetails(String id, String imgType, String type) {

        mSubscription = RXUtil.execute(mService.getGalleryDetails(id, imgType, type),
                new Observer<ResponseMessage<GalleryDetailModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<GalleryDetailModel> responseMessage) {
                        mView.showData(responseMessage.data.images);
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
     * 当前车系下的车型列表
     *
     * @param id
     */
    public void getCarList(String id) {

        mSubscription = RXUtil.execute(mService.getCarList(id),
                new Observer<ResponseMessage<List<CarBean>>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<CarBean>> responseMessage) {
                        mView.showData(responseMessage.data);
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
     * 厂商车系（二级品牌车系）
     *
     * @param params
     */
    public void getFactorySeries(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.getFactorySeries(params),
                new Observer<ResponseMessage<FactorySeriesModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<FactorySeriesModel> responseMessage) {
                        mView.showData(responseMessage.data);
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
     * 获取帖子带参标签数据
     *
     * @param params
     */
    public void getTopicTagDatas(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.getTopicTagDatas(params),
                new Observer<ResponseMessage<List<TopicTagDataModel>>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<TopicTagDataModel>> responseMessage) {
                        mView.showData(responseMessage.data);
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
