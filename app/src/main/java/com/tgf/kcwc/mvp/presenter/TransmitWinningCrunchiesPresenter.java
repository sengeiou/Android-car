package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.TransmitWinningCrunchiesBean;
import com.tgf.kcwc.mvp.model.TransmitWinningDetailsBean;
import com.tgf.kcwc.mvp.model.TransmitWinningHomeService;
import com.tgf.kcwc.mvp.view.TransmitWinningCrunchiesView;
import com.tgf.kcwc.mvp.view.TransmitWinningDetailsView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;

/**
 * Created by Administrator on 2017/4/21.
 */

public class TransmitWinningCrunchiesPresenter extends WrapPresenter<TransmitWinningCrunchiesView> {

    private TransmitWinningCrunchiesView mView;
    private TransmitWinningHomeService mService;

    @Override
    public void attachView(TransmitWinningCrunchiesView view) {
        mView=view;
        mService= ServiceFactory.getTransmitWinningHomeService();
    }

    public void getRecordLists(String token,String id){
        RXUtil.execute(mService.getRecordLists(token,id), new Observer<TransmitWinningCrunchiesBean>() {
            @Override
            public void onCompleted() {
                mView.getContext();
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
            }

            @Override
            public void onNext(TransmitWinningCrunchiesBean transmitWinningHomeListBean) {
                if (transmitWinningHomeListBean.code==0){
                    mView.dataListSucceed(transmitWinningHomeListBean);
                }else {
                    mView.dataListDefeated(transmitWinningHomeListBean.msg);
                }
            }
        });
    }

}
