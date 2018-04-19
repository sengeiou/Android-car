package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.RideAutoData;
import com.tgf.kcwc.mvp.model.RideData;
import com.tgf.kcwc.mvp.model.RideDataService;
import com.tgf.kcwc.mvp.model.RideLinePreviewModel;
import com.tgf.kcwc.mvp.model.RideReportDetailModel;
import com.tgf.kcwc.mvp.view.LoadCircuitView;
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

public class LoadCircuitPresenter extends WrapPresenter<LoadCircuitView> {
    private LoadCircuitView mView;
    private Subscription mSubscription;
    private RideDataService mService = null;

    @Override
    public void attachView(LoadCircuitView view) {
        this.mView = view;
        mService = ServiceFactory.getRideService();
    }

    /**
     * 加载重载路线数据 (不是规划的)
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
     * 加载预览线路 （规划的）
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
