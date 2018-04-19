package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.RideAutoData;
import com.tgf.kcwc.mvp.model.RideData;
import com.tgf.kcwc.mvp.model.RideDataService;
import com.tgf.kcwc.mvp.model.RideLinePreviewModel;
import com.tgf.kcwc.mvp.model.RideReportDetailModel;
import com.tgf.kcwc.mvp.view.RidingRunView;
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

public class RidingRunPresenter extends WrapPresenter<RidingRunView> {
    private RidingRunView mView;
    private Subscription mSubscription;
    private RideDataService mService = null;

    @Override
    public void attachView(RidingRunView view) {
        this.mView = view;
        mService = ServiceFactory.getRideService();
    }

    /**
     * 骑行开始
     */
    public void rideStart(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.rideStart(params),
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

                            mView.ridingStart(responseMessage);
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
     * 骑行暂停
     */
    public void ridePause(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.ridePause(params),
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

                            mView.ridingPause(responseMessage);
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
     * 骑行重新开始
     */
    public void rideRestart(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.rideRestart(params),
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

                            mView.ridingRestart(responseMessage.data);
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
     * 结束骑行
     */
    public void rideEnd(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.rideEnd(params),
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

                            mView.rodeEnd(responseMessage.data);
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
     * 上传骑行数据
     */
    public void autoCmtRideData(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.rideAutoData(params),
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

                            mView.autoCmtRideData(responseMessage);
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
     * 获取骑行状态
     */
    public void getRideState(String token) {

        mSubscription = RXUtil.execute(mService.getRideState(token),
                new Observer<ResponseMessage<RideAutoData>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<RideAutoData> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.getRideState(responseMessage.data);
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
     * 删除骑行路线
     */
    public void deleteRideData(String rideId, String token) {

        mSubscription = RXUtil.execute(mService.deleteRideData(rideId, token),
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

                            mView.deleteRideData(responseMessage.data);
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
     * 加载重载路线数据
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

                            mView.RideReportData(responseMessage.data);
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

                            mView.showloadRideDatas(responseMessage.data);
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
