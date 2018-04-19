package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.IntegralListBean;
import com.tgf.kcwc.mvp.model.IntegralService;
import com.tgf.kcwc.mvp.model.IntegralUserinfoBean;
import com.tgf.kcwc.mvp.model.OrgIntegralListBean;
import com.tgf.kcwc.mvp.view.IntegralView;
import com.tgf.kcwc.mvp.view.OrgIntegralView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class OrgIntegralPresenter extends WrapPresenter<OrgIntegralView> {

    private OrgIntegralView mView;
    private IntegralService mService;

    @Override
    public void attachView(OrgIntegralView view) {
        mView = view;
        mService = ServiceFactory.getIntegralService();
    }

    public void getRecordpList(String token,String type,int page) {
        RXUtil.execute(mService.getOrgRecords(token,"140",type,page), new Observer<OrgIntegralListBean>() {
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
            public void onNext(OrgIntegralListBean integralListBean) {
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

}
