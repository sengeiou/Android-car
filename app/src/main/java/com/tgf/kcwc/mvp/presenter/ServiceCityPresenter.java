package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.MotoDeatailService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.ServiceCity;
import com.tgf.kcwc.mvp.view.ServiceCityView;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/1/13 0013
 * E-mail:hekescott@qq.com
 */

public class ServiceCityPresenter implements BasePresenter<ServiceCityView> {
    private ServiceCityView    mView;
    private MotoDeatailService mService = null;
    private Subscription       mSubscription;

    @Override
    public void attachView(ServiceCityView view) {
        mView = view;
        mService = ServiceFactory.getMotoDetailService();
    }

    @Override
    public void detachView() {
        unsubscribe();
    }

    public void getServiceCity(String lat,String lng) {
        mSubscription = RXUtil.execute(mService.getOpenCity(lat,lng),
            new Observer<ResponseMessage<ServiceCity>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<ServiceCity> listResponseMessage) {
                    ServiceCity serviceCity = listResponseMessage.data;
                    mView.showOpenlist(serviceCity.list);
                    mView.showSelcetCity(serviceCity.select);
                }
            });

    }

    public void getServiceCity() {
        mSubscription = RXUtil.execute(mService.getOpenCity("",""),
            new Observer<ResponseMessage<ServiceCity>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<ServiceCity> listResponseMessage) {
                    ServiceCity serviceCity = listResponseMessage.data;
                    mView.showOpenlist(serviceCity.list);
                    mView.showSelcetCity(serviceCity.select);
                }
            });

    }

    public void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

    }

}
