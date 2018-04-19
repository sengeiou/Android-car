package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.RideData;
import com.tgf.kcwc.mvp.model.RideDataModel;
import com.tgf.kcwc.mvp.model.RideDataService;
import com.tgf.kcwc.mvp.model.RideLinePreviewModel;
import com.tgf.kcwc.mvp.model.RideReportDetailModel;
import com.tgf.kcwc.mvp.model.RideRunStateModel;
import com.tgf.kcwc.mvp.view.RideDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/8 20:55
 * E-mail：fishloveqin@gmail.com
 * 驾图Presenter
 */

public class RideDataPresenter extends WrapPresenter<RideDataView> {
    private RideDataView mView;
    private Subscription mSubscription;
    private RideDataService mService = null;

    @Override
    public void attachView(RideDataView view) {
        this.mView = view;
        mService = ServiceFactory.getRideService();
    }

    /**
     * 加载骑行数据
     */
    public void loadRideDatas(String page, String pageSize, String token) {

        mSubscription = RXUtil.execute(mService.getRideList(page, pageSize, token),
                new Observer<ResponseMessage<RideDataModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<RideDataModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
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
     * 加载规划数据
     */
    public void loadPlanDatas(String page, String pageSize, String token) {

        mSubscription = RXUtil.execute(mService.getPlanList(page, pageSize, token),
                new Observer<ResponseMessage<RideDataModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<RideDataModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
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
     * 加载收藏数据
     */
    public void loadFavoriteDatas(String page, String pageSize, String token) {

        mSubscription = RXUtil.execute(mService.getFavoriteList(page, pageSize, token),
                new Observer<ResponseMessage<RideDataModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<RideDataModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
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
     * 加载骑行报告数据
     */
    public void loadRideReportDatas(String rideId, String type, String token) {

        mSubscription = RXUtil.execute(mService.getRideReport(rideId, type, token),
                new Observer<ResponseMessage<RideReportDetailModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<RideReportDetailModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
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
     * 创建线路
     */
    public void createRidePlan(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.createRideLines(params),
                new Observer<ResponseMessage<RideData>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<RideData> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
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
     * 加载预览线路
     */
    public void loadRidePlanDatas(String rideId, String token) {

        mSubscription = RXUtil.execute(mService.loadRidePlan(rideId, token),
                new Observer<ResponseMessage<RideLinePreviewModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<RideLinePreviewModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
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
     * 获取驾图当前的状态 暂停、结束、异常
     */
    public void getRideRunState(String token) {

        mSubscription = RXUtil.execute(mService.checkRideRunState(token),
                new Observer<ResponseMessage<RideRunStateModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<RideRunStateModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
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
     * 删除规划的线路
     */
    public void deletePlanLine(String id, String token) {

        mSubscription = RXUtil.execute(mService.deletePlanLine(id, token),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
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
     * 生成封面
     */
    public void createCover(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.createCover(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
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
