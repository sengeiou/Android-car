package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.Motorshow;
import com.tgf.kcwc.mvp.model.MotorshowModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.VehicleService;
import com.tgf.kcwc.mvp.view.MotorshowView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/14 15:55
 * E-mail：fishloveqin@gmail.com
 * 品牌展会展车
 */

public class MotorshowPresenter extends WrapPresenter<MotorshowView> {
    private MotorshowView mView;
    private Subscription mSubscription;
    private VehicleService mService = null;

    @Override
    public void attachView(MotorshowView view) {
        this.mView = view;
        mService = ServiceFactory.getVehicleService();
    }

    /**
     * 获取品牌展车列表
     */
    public void loadBrandModels(String senseId, String hallId, String brandId, String page,
                                String pageSize, String isSpecial, String token) {

        mSubscription = RXUtil.execute(
                mService.brandModelsList(senseId, hallId, brandId, page, pageSize, isSpecial, token),
                new Observer<ResponseMessage<MotorshowModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<MotorshowModel> responseMessage) {

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
     * 获取品牌展车列表
     */
    public void loadBrandModels(Map<String, String> params) {

        mSubscription = RXUtil.execute(
                mService.brandModelsList(params),
                new Observer<ResponseMessage<MotorshowModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<MotorshowModel> responseMessage) {

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
     * 获取品牌展车列表图库
     */
    public void loadGallery(String id, String page, String pageSize, String token) {

        mSubscription = RXUtil.execute(mService.motorshowGallery(id, page, pageSize, token),
                new Observer<ResponseMessage<Motorshow>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Motorshow> responseMessage) {

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
