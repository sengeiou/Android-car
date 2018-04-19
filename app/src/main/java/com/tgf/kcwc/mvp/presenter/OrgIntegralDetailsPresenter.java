package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.IntegralService;
import com.tgf.kcwc.mvp.model.OrgIntegralDetailsBean;
import com.tgf.kcwc.mvp.view.IntegralPublishView;
import com.tgf.kcwc.mvp.view.OrgIntegralDetailsView;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class OrgIntegralDetailsPresenter extends WrapPresenter<OrgIntegralDetailsView> {

    private OrgIntegralDetailsView mView;
    private IntegralService mService;

    @Override
    public void attachView(OrgIntegralDetailsView view) {
        mView = view;
        mService = ServiceFactory.getIntegralService();
    }

    public void getOrgLogDetail(String token, String id) {
        RXUtil.execute(mService.getOrgLogDetail(token, id), new Observer<OrgIntegralDetailsBean>() {
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
            public void onNext(OrgIntegralDetailsBean integralListBean) {
                if (integralListBean.code == 0) {
                    mView.DataSucceed(integralListBean);
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
