package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CarGalleryModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.VehicleService;
import com.tgf.kcwc.mvp.view.CarGalleryView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/14 15:55
 * E-mail：fishloveqin@gmail.com
 * 图库
 */

public class CarGalleryPresenter extends WrapPresenter<CarGalleryView> {
    private CarGalleryView mView;
    private Subscription   mSubscription;
    private VehicleService mService = null;

    @Override
    public void attachView(CarGalleryView view) {
        this.mView = view;
        mService = ServiceFactory.getVehicleService();
    }

    /**
     *  
     * 获取图库信息
     * 
     */
    public void loadCarGallery(String id, String page, String pageSize) {

        mSubscription = RXUtil.execute(mService.newCarGallery(id, page, pageSize),
            new Observer<ResponseMessage<CarGalleryModel>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {

                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<CarGalleryModel> newCarLaunchModelResponseMessage) {

                    mView.showGallery(newCarLaunchModelResponseMessage.data);
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
