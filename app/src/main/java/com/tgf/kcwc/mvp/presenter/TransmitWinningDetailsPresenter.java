package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.TransmitDrawSucceedBean;
import com.tgf.kcwc.mvp.model.TransmitWinningDetailsBean;
import com.tgf.kcwc.mvp.model.TransmitWinningHomeService;
import com.tgf.kcwc.mvp.view.TransmitWinningDetailsView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;

/**
 * Created by Administrator on 2017/4/21.
 */

public class TransmitWinningDetailsPresenter extends WrapPresenter<TransmitWinningDetailsView> {

    private TransmitWinningDetailsView mView;
    private TransmitWinningHomeService mService;

    @Override
    public void attachView(TransmitWinningDetailsView view) {
        mView = view;
        mService = ServiceFactory.getTransmitWinningHomeService();
    }

    public void getDetails(String token, String id) {
        RXUtil.execute(mService.getDetails(token, id), new Observer<TransmitWinningDetailsBean>() {
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
            public void onNext(TransmitWinningDetailsBean transmitWinningHomeListBean) {
                if (transmitWinningHomeListBean.code == 0) {
                    mView.dataListSucceed(transmitWinningHomeListBean);
                } else {
                    mView.dataListDefeated(transmitWinningHomeListBean.msg);
                }
            }
        });
    }

    public void getForward(String token, String id, String type) {
        RXUtil.execute(mService.getForward(token, id, type), new Observer<TransmitDrawSucceedBean>() {
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
            public void onNext(TransmitDrawSucceedBean transmitDrawSucceedBean) {
                if (transmitDrawSucceedBean.code == 0) {
                    mView.dataForwardSucceed(transmitDrawSucceedBean);
                } else {
                    mView.dataListDefeated(transmitDrawSucceedBean.msg);
                }
            }
        });
    }

}
