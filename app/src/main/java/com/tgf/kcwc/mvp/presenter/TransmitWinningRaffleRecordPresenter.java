package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.RaffleRecordBean;
import com.tgf.kcwc.mvp.model.TransmitWinningDetailsBean;
import com.tgf.kcwc.mvp.model.TransmitWinningHomeService;
import com.tgf.kcwc.mvp.view.TransmitWinningRaffleRecordView;
import com.tgf.kcwc.mvp.view.TransmitWinningRaffleView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;

/**
 * Created by Administrator on 2017/4/21.
 */

public class TransmitWinningRaffleRecordPresenter extends WrapPresenter<TransmitWinningRaffleRecordView> {

    private TransmitWinningRaffleRecordView mView;
    private TransmitWinningHomeService mService;

    @Override
    public void attachView(TransmitWinningRaffleRecordView view) {
        mView=view;
        mService= ServiceFactory.getTransmitWinningHomeService();
    }

    public void getrecordDetails(String token,String id){
        RXUtil.execute(mService.getrecordDetails(token,id), new Observer<RaffleRecordBean>() {
            @Override
            public void onCompleted() {
                mView.getContext();
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
            }

            @Override
            public void onNext(RaffleRecordBean raffleRecordBean) {
                if (raffleRecordBean.code==0){
                    mView.dataListSucceed(raffleRecordBean);
                }else {
                    mView.dataListDefeated(raffleRecordBean.msg);
                }
            }
        });
    }


}
