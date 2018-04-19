package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CarOwnerSalerModel;
import com.tgf.kcwc.mvp.model.DaRenEvaluateModel;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.mvp.model.OrgModel;
import com.tgf.kcwc.mvp.model.MotoDeatailService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.MotordetailView;
import com.tgf.kcwc.util.RXUtil;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2016/12/13 0013
 * E-mail:hekescott@qq.com
 */

public class MotoDetailPresenter extends WrapPresenter<MotordetailView> {
    private MotordetailView mView;

    private MotoDeatailService mService = null;

    private CarBean mMotoDetail;
    private final int POWER_FORMS_TYPE_BATTERY = 2;

    @Override
    public void attachView(MotordetailView view) {
        mView = view;
        mService = ServiceFactory.getMotoDetailService();
    }

    public void getMotoDeatail(String motoid, String token) {
        Subscription mSubscription = RXUtil.execute(mService.getMotoDetail(motoid, token),
                new Observer<ResponseMessage<CarBean>>() {
                    @Override
                    public void onCompleted() {
//                    if (mMotoDetail != null) {
//                        if (mMotoDetail.powerForms == POWER_FORMS_TYPE_BATTERY) {
//                            mView.showMotoBatterryMoto(mMotoDetail);
//                        } else {
//                            mView.showMotoOilMoto(mMotoDetail);
//                        }
//                    }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseMessage<CarBean> motoResponseMessage) {
                        mMotoDetail = motoResponseMessage.data;
                        mView.showMotoDeatail(mMotoDetail);
                    }
                });
        mSubscriptions.add(mSubscription);

    }


    public void getORGList(int brandid, String longitude, String latitude) {
        Subscription mSubscription = RXUtil.execute(mService.getExhibitOrgModel(brandid, longitude, latitude),
                new Observer<ResponseMessage<ArrayList<OrgModel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.failure(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseMessage<ArrayList<OrgModel>> arrayListResponseMessage) {
                        mView.showORGlist(arrayListResponseMessage.getData());
                    }
                });
        mSubscriptions.add(mSubscription);
    }

    public void getDaRenEvaluateList(int carID) {
        Subscription mSubscription = RXUtil.execute(mService.getDarenEvaluate(carID), new Observer<ResponseMessage<ArrayList<DaRenEvaluateModel>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<ArrayList<DaRenEvaluateModel>> arrayListResponseMessage) {
                mView.showDarenEvaluate(arrayListResponseMessage.getData());
            }
        });

        mSubscriptions.add(mSubscription);
    }
    public void getCarOwnerList(int carID) {
        Subscription mSubscription = RXUtil.execute(mService.getCarOwnerList(carID), new Observer<ResponseMessage<ArrayList<CarOwnerSalerModel>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<ArrayList<CarOwnerSalerModel>> arrayListResponseMessage) {
                mView.showCarOwnerList(arrayListResponseMessage.getData());
            }
        });

        mSubscriptions.add(mSubscription);
    }
}
