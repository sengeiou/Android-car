package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.IntegralExchangeSucceedBean;
import com.tgf.kcwc.mvp.model.IntegralPurchaseRecordListBean;
import com.tgf.kcwc.mvp.model.IntegralRecordBean;
import com.tgf.kcwc.mvp.model.IntegralRecordListBean;
import com.tgf.kcwc.mvp.model.IntegralService;
import com.tgf.kcwc.mvp.view.IntegralPublishView;
import com.tgf.kcwc.mvp.view.IntegralRecordView;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class IntegralPublishPresenter extends WrapPresenter<IntegralPublishView> {

    private IntegralPublishView mView;
    private IntegralService mService;

    @Override
    public void attachView(IntegralPublishView view) {
        mView = view;
        mService = ServiceFactory.getIntegralService();
    }

    public void getPointsReceiveList(Map<String, String> params) {
        RXUtil.execute(mService.getAddGoods(params), new Observer<BaseArryBean>() {
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
            public void onNext(BaseArryBean integralListBean) {
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
