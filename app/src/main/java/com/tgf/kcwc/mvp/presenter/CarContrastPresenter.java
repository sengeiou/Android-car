package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CarContrastModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.VehicleService;
import com.tgf.kcwc.mvp.view.CarContrastView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2017/1.4
 * E-mail：fishloveqin@gmail.com
 * 车型对比
 */

public class CarContrastPresenter extends WrapPresenter<CarContrastView> {
    private CarContrastView mView;
    private Subscription    mSubscription;
    private VehicleService  mService = null;

    @Override
    public void attachView(CarContrastView view) {
        this.mView = view;
        mService = ServiceFactory.getVehicleService();
    }

    /**
     *  
     * 获取车型对比参数列表
     * 
     */
    public void loadContrastList(String carIds,String token) {

        mSubscription = RXUtil.execute(mService.contrastList(carIds,token),
            new Observer<ResponseMessage<CarContrastModel>>() {
                @Override
                public void onCompleted() {

                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ResponseMessage<CarContrastModel> listResponseMessage) {

                    mView.showContrastView(listResponseMessage.data);
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
