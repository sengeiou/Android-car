package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.RideDataModel;
import com.tgf.kcwc.mvp.model.TripBookService;
import com.tgf.kcwc.mvp.view.SelectRoadlLineView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/5/5 0005
 * E-mail:hekescott@qq.com
 */

public class SelectRoadLinePresenter extends WrapPresenter<SelectRoadlLineView> {

    private SelectRoadlLineView mView;
    private TripBookService mService;

    @Override
    public void attachView(SelectRoadlLineView view) {
        mView = view;
        mService = ServiceFactory.getTripBookService();
    }

    public void getRoadLinelist(String token){
        RXUtil.execute(mService.getRideLines(token), new Observer<ResponseMessage<RideDataModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<RideDataModel> rideDataModelResponseMessage) {
                int rideId = rideDataModelResponseMessage.data.ride_id;
                if (rideId == 0) {
                    mView.showRoadLines(rideDataModelResponseMessage.data.list);
                } else {
                    if(rideDataModelResponseMessage.data.status==0){//骑行中
                        mView.showUnStop(rideDataModelResponseMessage.getData());
                    }else{//骑行中暂停状态
                        mView.showContinue(rideDataModelResponseMessage.getData());
                    }

                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
}
