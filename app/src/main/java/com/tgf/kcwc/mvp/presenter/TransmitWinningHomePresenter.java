package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.TransmitWinningHomeListBean;
import com.tgf.kcwc.mvp.model.TransmitWinningHomeService;
import com.tgf.kcwc.mvp.view.TransmitWinningHomeView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class TransmitWinningHomePresenter extends WrapPresenter<TransmitWinningHomeView> {

    private TransmitWinningHomeView mView;
    private TransmitWinningHomeService mService;

    @Override
    public void attachView(TransmitWinningHomeView view) {
        mView=view;
        mService= ServiceFactory.getTransmitWinningHomeService();
    }

    public void gainAppLsis(String token,int page){
        RXUtil.execute(mService.getIndexLists(token,page), new Observer<TransmitWinningHomeListBean>() {
            @Override
            public void onCompleted() {
                mView.getContext();
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(TransmitWinningHomeListBean transmitWinningHomeListBean) {
                if (transmitWinningHomeListBean.code==0){
                    mView.dataListSucceed(transmitWinningHomeListBean);
                }else {
                    mView.dataListDefeated(transmitWinningHomeListBean.msg);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
    public void getUserLists(String token,int page){
        RXUtil.execute(mService.getUserLists(token,page), new Observer<TransmitWinningHomeListBean>() {
            @Override
            public void onCompleted() {
                mView.getContext();
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(TransmitWinningHomeListBean transmitWinningHomeListBean) {
                if (transmitWinningHomeListBean.code==0){
                    mView.dataListSucceed(transmitWinningHomeListBean);
                }else {
                    mView.dataListDefeated(transmitWinningHomeListBean.msg);
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
