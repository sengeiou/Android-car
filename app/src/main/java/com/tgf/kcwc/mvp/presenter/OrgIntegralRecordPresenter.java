package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.IntegralPurchaseRecordListBean;
import com.tgf.kcwc.mvp.model.IntegralRecordBean;
import com.tgf.kcwc.mvp.model.IntegralRecordListBean;
import com.tgf.kcwc.mvp.model.IntegralService;
import com.tgf.kcwc.mvp.model.OrgIntegralRecordListBean;
import com.tgf.kcwc.mvp.view.IntegralRecordView;
import com.tgf.kcwc.mvp.view.OrgIntegralRecordView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class OrgIntegralRecordPresenter extends WrapPresenter<OrgIntegralRecordView> {

    private OrgIntegralRecordView mView;
    private IntegralService mService;

    @Override
    public void attachView(OrgIntegralRecordView view) {
        mView = view;
        mService = ServiceFactory.getIntegralService();
    }

    public void getOrgLogRecords(String token, int page) {
        RXUtil.execute(mService.getOrgLogRecords(token, "car", page), new Observer<OrgIntegralRecordListBean>() {
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
            public void onNext(OrgIntegralRecordListBean integralListBean) {
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

}
