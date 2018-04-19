package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.DrivDetailsBean;
import com.tgf.kcwc.mvp.model.DrivingRoadBookBean;
import com.tgf.kcwc.mvp.model.DrivingService;
import com.tgf.kcwc.mvp.view.DrivingDetailsView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/24.
 */

public class DrivingDetailsPresenter extends WrapPresenter<DrivingDetailsView> {

    private DrivingDetailsView mView;
    private DrivingService     mService;

    @Override
    public void attachView(DrivingDetailsView view) {
        mView = view;
        mService = ServiceFactory.getDrivingService();
    }

    public void gainDetailsData(String token, String id) {
        RXUtil.execute(mService.getDetailsData(token, id), new Observer<DrivDetailsBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.detailsDataFeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(DrivDetailsBean drivDetailsBean) {
                if (drivDetailsBean.code==0){
                    mView.detailsDataSucceed(drivDetailsBean);
                }else {
                    mView.detailsDataFeated(drivDetailsBean.msg);
                }

            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void getDetail(String token, String id) {
        RXUtil.execute(mService.getDetails(token, id), new Observer<DrivingRoadBookBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.detailsDataFeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(DrivingRoadBookBean drivingRoadBookBean) {

                if (drivingRoadBookBean.code==0){
                    mView.DetailsSucceed(drivingRoadBookBean);
                }else {
                    mView.detailsDataFeated(drivingRoadBookBean.msg);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void getApplyCancel(String token, String id) {
        RXUtil.execute(mService.getApplyCancel(token, id), new Observer<BaseBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.detailsDataFeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(BaseBean baseBean) {

                if (baseBean.code==0){
                    mView.ApplyCancelSucceed(baseBean);
                }else {
                    mView.detailsDataFeated(baseBean.msg);
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
