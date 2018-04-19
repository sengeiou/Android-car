package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.MeCrunchiesBean;
import com.tgf.kcwc.mvp.model.TransmitWinningHomeService;
import com.tgf.kcwc.mvp.view.MeRaffleRecordView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class MeRaffleRecordPresenter extends WrapPresenter<MeRaffleRecordView> {

    private MeRaffleRecordView mView;
    private TransmitWinningHomeService mService;

    @Override
    public void attachView(MeRaffleRecordView view) {
        mView=view;
        mService= ServiceFactory.getTransmitWinningHomeService();
    }

    public void getIndexList(String token,String type){
        RXUtil.execute(mService.getIndexList(token,type), new Observer<MeCrunchiesBean>() {
            @Override
            public void onCompleted() {
                mView.getContext();
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(MeCrunchiesBean meCrunchiesBean) {
                if (meCrunchiesBean.code==0){
                    mView.dataListSucceed(meCrunchiesBean);
                }else {
                    mView.dataListDefeated(meCrunchiesBean.msg);
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
