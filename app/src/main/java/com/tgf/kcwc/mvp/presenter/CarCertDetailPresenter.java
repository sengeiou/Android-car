package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CarCertDetailModel;
import com.tgf.kcwc.mvp.model.CarCertDetailService;
import com.tgf.kcwc.mvp.model.CertDetailModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CarCertDetailView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/10/18
 * @describle
 */
public class CarCertDetailPresenter extends WrapPresenter<CarCertDetailView> {
    Subscription mSubscription;
    CarCertDetailView mView;
    CarCertDetailService mService;
    @Override
    public void attachView(CarCertDetailView view) {
        mView = view;
        mService = ServiceFactory.getCarCertDetailService();
    }

    /**
     * 获取新增证件详情数据
     * @param isNeedReceive
     * @param id
     * @param isNeedTotal
     * @param code
     * @param token
     */
    public void getCertDetail(String isNeedReceive, String id, String isNeedTotal, String code, String token){
        mSubscription = RXUtil.execute(
                mService.getCertDetail(isNeedReceive, id, isNeedTotal, code, token),
                new Observer<ResponseMessage<CarCertDetailModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<CarCertDetailModel> responseMessage) {
                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS){
                            mView.showCertDetail(responseMessage.data);
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
