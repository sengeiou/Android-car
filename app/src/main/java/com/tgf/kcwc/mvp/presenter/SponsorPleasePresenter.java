package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.PleaseService;
import com.tgf.kcwc.mvp.view.SponsorPleaseView;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2017/4/21.
 */

public class SponsorPleasePresenter extends WrapPresenter<SponsorPleaseView> {

    private SponsorPleaseView mView;
    private PleaseService mService;

    @Override
    public void attachView(SponsorPleaseView view) {
        mView = view;
        mService = ServiceFactory.getPleaseService();
    }

    public void getCreate(Map<String, String> params) {
        RXUtil
            .execute(
                mService.getCreate(params),
                new Observer<BaseBean>() {
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
