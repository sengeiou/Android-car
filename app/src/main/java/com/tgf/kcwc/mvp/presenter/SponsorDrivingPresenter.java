package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.DrivingService;
import com.tgf.kcwc.mvp.view.SponsorDrivingView;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2017/4/21.
 */

public class SponsorDrivingPresenter extends WrapPresenter<SponsorDrivingView> {

    private SponsorDrivingView mView;
    private DrivingService mService;

    @Override
    public void attachView(SponsorDrivingView view) {
        mView = view;
        mService = ServiceFactory.getDrivingService();
    }

    public void getCreate(Map<String, String> params) {
        RXUtil.execute(mService.getCreate(params), new Observer<BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(BaseBean applyListBean) {
                if (applyListBean.code == 0) {
                    mView.dataListSucceed();
                } else {
                    mView.dataListDefeated(applyListBean.msg);
                }
            }
        });
    }
}
