package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.DrivingRoadBookBean;
import com.tgf.kcwc.mvp.model.PleaseDetailsBean;
import com.tgf.kcwc.mvp.model.PleaseService;
import com.tgf.kcwc.mvp.view.PleaseDetailsView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/28.
 */

public class PleaseDetailsPrenter extends WrapPresenter<PleaseDetailsView> {


    private PleaseDetailsView   mView;
    private PleaseService mService;

    @Override
    public void attachView(PleaseDetailsView view) {
        mView = view;
        mService = ServiceFactory.getPleaseService();
    }
    public void gainDetailsData(String token, String id) {
        RXUtil.execute(mService.getDetailsData(token, id), new Observer<PleaseDetailsBean>() {
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
            public void onNext(PleaseDetailsBean pleaseDetailsBean) {
                if (pleaseDetailsBean.code==0){
                    mView.dataListSucceed(pleaseDetailsBean);
                }else {
                    mView.dataListDefeated(pleaseDetailsBean.msg);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
    public void getDetail(String token, String id) {
        RXUtil.execute(mService.getDetails(token, id), new Observer<DrivingRoadBookBean>() {
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
            public void onNext(DrivingRoadBookBean drivingRoadBookBean) {
                mView.DetailsSucceed(drivingRoadBookBean);
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void getApplyCancel(String token, String id) {
        RXUtil.execute(mService.getApplyCancel(token, id), new Observer<BaseBean>() {
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
            public void onNext(BaseBean baseBean) {

                if (baseBean.code==0){
                    mView.ApplyCancelSucceed(baseBean);
                }else {
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
