package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.IntegralConversionGoodDetailsBean;
import com.tgf.kcwc.mvp.model.IntegralExchangeBean;
import com.tgf.kcwc.mvp.model.IntegralExchangeSucceedBean;
import com.tgf.kcwc.mvp.model.IntegralGoodListBean;
import com.tgf.kcwc.mvp.model.IntegralService;
import com.tgf.kcwc.mvp.model.IntegralUserinfoBean;
import com.tgf.kcwc.mvp.view.IntegralConversionSucceedView;
import com.tgf.kcwc.mvp.view.IntegralConversionView;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class IntegralConversionSucceedPresenter extends WrapPresenter<IntegralConversionSucceedView> {

    private IntegralConversionSucceedView mView;
    private IntegralService mService;

    @Override
    public void attachView(IntegralConversionSucceedView view) {
        mView = view;
        mService = ServiceFactory.getIntegralService();
    }


    public void getRecordDetail(String id) {
        RXUtil.execute(mService.getRecordDetail(id), new Observer<IntegralExchangeSucceedBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
            }

            @Override
            public void onNext(IntegralExchangeSucceedBean baseBean) {
                if (baseBean.code == 0) {
                    mView.DataBuyProductSucceed(baseBean);
                } else {
                    mView.dataListDefeated(baseBean.msg);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }


    public void getEditAddress(Map<String, String> params) {
        RXUtil.execute(mService.getEditAddress(params), new Observer<IntegralExchangeSucceedBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
            }

            @Override
            public void onNext(IntegralExchangeSucceedBean baseBean) {
                if (baseBean.code == 0) {
                    mView.DataBuyProductSucceed(baseBean);
                } else {
                    mView.dataListDefeated(baseBean.msg);
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
