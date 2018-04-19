package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CarColor;
import com.tgf.kcwc.mvp.model.GalleryDetailModel;
import com.tgf.kcwc.mvp.model.MotoBuyService;
import com.tgf.kcwc.mvp.model.MotoOrder;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.VehicleService;
import com.tgf.kcwc.mvp.view.CommitOrdeView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/2/20 0020
 * E-mail:hekescott@qq.com
 */

public class CommitMotoOrderPresenter extends WrapPresenter<CommitOrdeView> {
    private CommitOrdeView mView;
    private MotoBuyService motoBuyService;
    private VehicleService mVehicleService ;
    @Override
    public void attachView(CommitOrdeView view) {
        mView = view;
        motoBuyService = ServiceFactory.getMotoBuyService();
        mVehicleService = ServiceFactory.getVehicleService();
    }

    public void commitMotoOrder(String token, int carId,int carSeriesId,int inLookColorId,int outLookColorId, String contact, String description,
                                String lat, String log, String tel) {
        RXUtil.execute(
            motoBuyService.commitMotoOrder(token, carId,carSeriesId,inLookColorId,outLookColorId, contact, description, lat, log, tel),
            new Observer<ResponseMessage<MotoOrder>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {
//                    mView.showCommitOrderFailed("提交失败");
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onNext(ResponseMessage<MotoOrder> motoOrderResponseMessage) {
                    if (motoOrderResponseMessage.statusCode == 0) {
                        mView.showSuccess(motoOrderResponseMessage.getData().OrderId);
                    } else {
                        mView.showCommitOrderFailed(motoOrderResponseMessage.statusMessage);
                    }
                }

                //            @Override
                //            public void onNext(ResponseMessage responseMessage) {
                //                    if (responseMessage.statusCode==0){
                //                        mView.showSuccess();
                //                    }else {
                //                        mView.showLoadingTasksError();
                //                    }
                //            }
            }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                });
    }

    /**
     * 外观颜色
     */
    public void getCarOutLookColors(String id, String type ) {

        Subscription subscription = RXUtil.execute(mVehicleService.getCarCategoryColors(id, type, "out"),
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
                            mView.showOutLook(responseMessage.data);
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

        mSubscriptions.add(subscription);
    }
    /**
     * 内饰颜色
     */
    public void getCarInLookColors(String id, String type ) {

        Subscription subscription = RXUtil.execute(mVehicleService.getCarCategoryColors(id, type, "in"),
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
                            mView.showInLook(responseMessage.data);
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

        mSubscriptions.add(subscription);
    }

}
