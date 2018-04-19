package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.TransmWinningResultBean;
import com.tgf.kcwc.mvp.model.TransmitWinningHomeService;
import com.tgf.kcwc.mvp.model.TransmitWinningRaffleBean;
import com.tgf.kcwc.mvp.view.TransmitWinningRaffleView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class TransmitWinningRafflePresenter extends WrapPresenter<TransmitWinningRaffleView> {

    private TransmitWinningRaffleView mView;
    private TransmitWinningHomeService mService;

    @Override
    public void attachView(TransmitWinningRaffleView view) {
        mView=view;
        mService= ServiceFactory.getTransmitWinningHomeService();
    }

    public void getPrizeList(String token,String id){
        RXUtil.execute(mService.getPrizeList(token,id), new Observer<TransmitWinningRaffleBean>() {
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
            public void onNext(TransmitWinningRaffleBean transmitWinningHomeListBean) {
                if (transmitWinningHomeListBean.code==0){
                    mView.dataListSucceed(transmitWinningHomeListBean);
                }else {
                    mView.dataListDefeated(transmitWinningHomeListBean.msg);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                //mView.setLoadingIndicator(true);
            }
        });
    }

    public void getForward(String token,String id,String type){
        RXUtil.execute(mService.getForward2(token,id,type), new Observer<TransmWinningResultBean>() {
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
            public void onNext(TransmWinningResultBean transmitWinningHomeListBean) {
                if (transmitWinningHomeListBean.code==0){
                    mView.dataForwardSucceed(transmitWinningHomeListBean);
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
