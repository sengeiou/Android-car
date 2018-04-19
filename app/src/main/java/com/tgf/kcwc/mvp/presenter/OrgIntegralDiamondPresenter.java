package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.IntegralDiamondListBean;
import com.tgf.kcwc.mvp.model.IntegralPurchaseBean;
import com.tgf.kcwc.mvp.model.IntegralService;
import com.tgf.kcwc.mvp.model.OrgDetailsBean;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.OrgIntegralDiamondView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;

/**
 * Created by Administrator on 2017/4/21.
 */

public class OrgIntegralDiamondPresenter extends WrapPresenter<OrgIntegralDiamondView> {

    private OrgIntegralDiamondView mView;
    private IntegralService mService;

    @Override
    public void attachView(OrgIntegralDiamondView view) {
        mView = view;
        mService = ServiceFactory.getIntegralService();
    }

    public void getOrgDetail(String token,String org_id, String type) {
        RXUtil.execute(mService.getOrgDetail(token,org_id, "car"), new Observer<OrgDetailsBean>() {
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
            public void onNext(OrgDetailsBean integralUserinfoBean) {
                if (integralUserinfoBean.code == 0) {
                    mView.userDataSucceed(integralUserinfoBean);
                } else {
                    mView.dataListDefeated(integralUserinfoBean.msg);
                }
            }
        });
    }

    public void getGoodList(String token, String type, int page) {
        RXUtil.execute(mService.getGoodsList(token, type, "car", page), new Observer<IntegralDiamondListBean>() {
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
            public void onNext(IntegralDiamondListBean integralGoodListBean) {
                if (integralGoodListBean.code == 0) {
                    mView.DatalistSucceed(integralGoodListBean);
                } else {
                    mView.dataListDefeated(integralGoodListBean.msg);
                }
            }
        });
    }

    public void getGoodsDetail(String token, String id) {
        RXUtil.execute(mService.getGoodsDetail(token, id), new Observer<IntegralPurchaseBean>() {
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
            public void onNext(IntegralPurchaseBean integralGoodListBean) {
                if (integralGoodListBean.code == 0) {
                    mView.DataPurchaseSucceed(integralGoodListBean);
                } else {
                    mView.dataListDefeated(integralGoodListBean.msg);
                }
            }
        });
    }

    public void getBuyGoods(String token, String id) {
        RXUtil.execute(mService.getBuyGoods(token, id), new Observer<ResponseMessage<String>>() {
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
            public void onNext(ResponseMessage<String> integralGoodListBean) {
                if (integralGoodListBean.statusCode == 0) {
                    mView.DataOrdeSucceed(integralGoodListBean);
                } else {
                    mView.dataListDefeated(integralGoodListBean.statusMessage);
                }
            }
        });
    }
}
