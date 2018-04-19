package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.DrivingRoadBookBean;
import com.tgf.kcwc.mvp.model.QrcodeSiginBean;
import com.tgf.kcwc.mvp.model.SignInService;
import com.tgf.kcwc.mvp.view.SignInView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/24.
 */

public class SignInPresenter extends WrapPresenter<SignInView> {

    private SignInView mView;
    private SignInService mService;

    @Override
    public void attachView(SignInView view) {
        mView = view;
        mService = ServiceFactory.getSignInService();
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
                if (drivingRoadBookBean.code == 0) {
                    mView.DetailsSucceed(drivingRoadBookBean);
                } else {
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

    public void getLight(String token, String id) {
        RXUtil.execute(mService.getLight(token, id), new Observer<BaseBean>() {
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
                if (baseBean.code == 0) {
                    mView.LightenSucceed(baseBean);
                } else {
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

    public void getCreateCheck(String token,  String ride_check_id) {
        RXUtil.execute(mService.getCreateCheck(token, ride_check_id), new Observer<QrcodeSiginBean>() {
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
            public void onNext(QrcodeSiginBean qrcodeSiginBean) {
                if (qrcodeSiginBean.code == 0) {
                    mView.CreateChecksSucceed(qrcodeSiginBean);
                } else {
                    mView.detailsDataFeated(qrcodeSiginBean.msg);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void getEndAcitivity(String token, String id) {
        RXUtil.execute(mService.getEndAcitivity(token, id), new Observer<BaseArryBean>() {
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
            public void onNext(BaseArryBean baseBean) {
                if (baseBean.code == 0) {
                    mView.EndAcitivitySucceed(baseBean);
                } else {
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
