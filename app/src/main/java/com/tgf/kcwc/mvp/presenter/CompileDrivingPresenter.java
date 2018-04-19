package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.CompileDrivingBean;
import com.tgf.kcwc.mvp.model.DrivingService;
import com.tgf.kcwc.mvp.view.CompileDrivingView;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class CompileDrivingPresenter extends WrapPresenter<CompileDrivingView> {

    private CompileDrivingView mView;
    private DrivingService mService;

    @Override
    public void attachView(CompileDrivingView view) {
        mView = view;
        mService = ServiceFactory.getDrivingService();
    }

    public void getCreate(Map<String, String> params) {
        RXUtil.execute(mService.getEdit(params), new Observer<BaseBean>() {
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
            public void onNext(BaseBean applyListBean) {
                if (applyListBean.code == 0) {
                    mView.dataListSucceed();
                } else {
                    mView.dataListDefeated(applyListBean.msg);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void getEdit(String token, String id) {
        RXUtil.execute(mService.getEdit(token, id), new Observer<CompileDrivingBean>() {
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
            public void onNext(CompileDrivingBean compileDrivingBean) {
                if (compileDrivingBean.code == 0) {
                    mView.dataSucceed(compileDrivingBean);
                } else {
                    mView.dataDefeated(compileDrivingBean.msg);
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
