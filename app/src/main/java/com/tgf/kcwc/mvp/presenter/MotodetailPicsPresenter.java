package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CarModelPicsModel;
import com.tgf.kcwc.mvp.model.MotoDeatailService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.MotoDetailPicsView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/1/20 0020
 * E-mail:hekescott@qq.com
 */

public class MotodetailPicsPresenter extends WrapPresenter<MotoDetailPicsView> {

    private MotoDetailPicsView mView;
    private MotoDeatailService mService;

    @Override
    public void attachView(MotoDetailPicsView view) {
        mView = view;
        mService = ServiceFactory.getMotoDetailService();
    }

    public void  getMotoDetailPics(int carId){
      Subscription subscription = RXUtil.execute(mService.getCarModelCatgoryImg(carId), new Observer<ResponseMessage<CarModelPicsModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showLoadingTasksError();
            }

            @Override
            public void onNext(ResponseMessage<CarModelPicsModel> carModelPicsModelResponseMessage) {
                CarModelPicsModel carModelPicsModel = carModelPicsModelResponseMessage.getData();
                if (carModelPicsModel !=null){
                    mView.showInterior(carModelPicsModel.interiorImglist);
                    mView.showWholeCar(carModelPicsModel.appearanceImglist);
                    mView.showLiving(carModelPicsModel.xcImglist);
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
