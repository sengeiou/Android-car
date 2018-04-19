package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.IntegralConversionGoodDetailsBean;
import com.tgf.kcwc.mvp.model.IntegralExchangeBean;
import com.tgf.kcwc.mvp.model.IntegralGoodListBean;
import com.tgf.kcwc.mvp.model.IntegralListBean;
import com.tgf.kcwc.mvp.model.IntegralService;
import com.tgf.kcwc.mvp.model.IntegralUserinfoBean;
import com.tgf.kcwc.mvp.view.IntegralConversionView;
import com.tgf.kcwc.mvp.view.IntegralView;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class IntegralConversionPresenter extends WrapPresenter<IntegralConversionView> {

    private IntegralConversionView mView;
    private IntegralService mService;

    @Override
    public void attachView(IntegralConversionView view) {
        mView = view;
        mService = ServiceFactory.getIntegralService();
    }

    public void getUserinfo(String token,String type) {
        RXUtil.execute(mService.getUserinfo(token,type,"car"), new Observer<IntegralUserinfoBean>() {
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
            public void onNext(IntegralUserinfoBean integralUserinfoBean) {
                if (integralUserinfoBean.code == 0) {
                    mView.userDataSucceed(integralUserinfoBean);
                } else {
                    mView.dataListDefeated(integralUserinfoBean.msg);
                }
            }
        });
    }

    public void getGoodList(String token, String type,int page) {
        RXUtil.execute(mService.getPointsshop(token, type,"car",page), new Observer<IntegralGoodListBean>() {
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
            public void onNext(IntegralGoodListBean integralGoodListBean) {
                if (integralGoodListBean.code == 0) {
                    mView.DatalistSucceed(integralGoodListBean);
                } else {
                    mView.dataListDefeated(integralGoodListBean.msg);
                }
            }
        });
    }

    public void getGooddetails(String token, String id) {
        RXUtil.execute(mService.getGooddetails(token, id), new Observer<IntegralConversionGoodDetailsBean>() {
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
            public void onNext(IntegralConversionGoodDetailsBean integralConversionGoodDetailsBean) {
                if (integralConversionGoodDetailsBean.code == 0) {
                    mView.DataDetailsSucceed(integralConversionGoodDetailsBean);
                } else {
                    mView.dataListDefeated(integralConversionGoodDetailsBean.msg);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void getBuyProduct(Map<String, String> params) {
        RXUtil.execute(mService.getBuyProduct(params), new Observer<IntegralExchangeBean>() {
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
            public void onNext(IntegralExchangeBean baseBean) {
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
