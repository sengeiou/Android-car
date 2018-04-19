package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.MotoParamModel;
import com.tgf.kcwc.mvp.model.MotoParamService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.MotoParamView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/22 16:55
 * E-mail：fishloveqin@gmail.com
 * 店铺主页Presenter
 */

public class MotoParamPresenter extends WrapPresenter<MotoParamView> {
    private MotoParamView  mView;
    private Subscription   mSubscription;
    private MotoParamService mService = null;

    public void attachView(MotoParamView view) {
        this.mView = view;
        mService = ServiceFactory.getMotoParamService();
    }

    public void loadMotoParam(String motoid) {

        mSubscription = RXUtil.execute(mService.getMotoParam(motoid),
            new Observer<ResponseMessage<MotoParamModel>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {

                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<MotoParamModel> motoParamModelResponseMessage) {
                    MotoParamModel motoParamModel =   motoParamModelResponseMessage.data;
                    mView.showData(motoParamModel.myparamList);
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
     *
     *获取店铺信息
     *@param  id 
     */
//    public void loadStoreInfo(String id) {
//
//        mSubscription = RXUtil.execute(mService.storeDetail(id),
//            new Observer<ResponseMessage<StoreDetailData>>() {
//                @Override
//                public void onCompleted() {
//                    mView.setLoadingIndicator(false);
//                }
//
//                @Override
//                public void onError(Throwable e) {
//
//                    mView.showLoadingTasksError();
//                }
//
//                @Override
//                public void onNext(ResponseMessage<StoreDetailData> repData) {
//
//                    mView.showStoreInfo(repData.data);
//                }
//            }, new Action0() {
//                @Override
//                public void call() {
//                    mView.setLoadingIndicator(true);
//                }
//            });
//        mSubscriptions.add(mSubscription);
//    }

}
