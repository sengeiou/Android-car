package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.IntegralDiamondListBean;
import com.tgf.kcwc.mvp.model.IntegralListBean;
import com.tgf.kcwc.mvp.model.IntegralPurchaseBean;
import com.tgf.kcwc.mvp.model.IntegralPurchaseRecordListBean;
import com.tgf.kcwc.mvp.model.IntegralRecordBean;
import com.tgf.kcwc.mvp.model.IntegralRecordListBean;
import com.tgf.kcwc.mvp.model.IntegralService;
import com.tgf.kcwc.mvp.model.IntegralUserinfoBean;
import com.tgf.kcwc.mvp.view.IntegralRecordView;
import com.tgf.kcwc.mvp.view.IntegralView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class IntegralRecordPresenter extends WrapPresenter<IntegralRecordView> {

    private IntegralRecordView mView;
    private IntegralService mService;

    @Override
    public void attachView(IntegralRecordView view) {
        mView = view;
        mService = ServiceFactory.getIntegralService();
    }

    public void getPointsReceiveList(String token, int page) {
        RXUtil.execute(mService.getPointsReceiveList(token, "car", page), new Observer<IntegralRecordListBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(IntegralRecordListBean integralListBean) {
                if (integralListBean.code == 0) {
                    mView.DatalistSucceed(integralListBean);
                } else {
                    mView.dataListDefeated(integralListBean.msg);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void getRecordDetail(String token, String id) {
        RXUtil.execute(mService.getRecordDetail(token, id), new Observer<IntegralRecordBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(IntegralRecordBean integralListBean) {
                if (integralListBean.code == 0) {
                    mView.RecordSucceed(integralListBean);
                } else {
                    mView.dataListDefeated(integralListBean.msg);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void getLogRecords(String token, int page) {
        RXUtil.execute(mService.getLogRecords(token,page), new Observer<IntegralPurchaseRecordListBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(IntegralPurchaseRecordListBean integralListBean) {
                if (integralListBean.code == 0) {
                      mView.PurchaseRecordSucceed(integralListBean);
                } else {
                    mView.dataListDefeated(integralListBean.msg);
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
