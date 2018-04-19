package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.CompilePleaseBean;
import com.tgf.kcwc.mvp.model.PleaseService;
import com.tgf.kcwc.mvp.view.CompilePleaseView;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class CompilePleasePresenter extends WrapPresenter<CompilePleaseView> {

    private CompilePleaseView mView;
    private PleaseService mService;

    @Override
    public void attachView(CompilePleaseView view) {
        mView = view;
        mService = ServiceFactory.getPleaseService();
    }

    public void getCreate(Map<String, String> params) {
        RXUtil
            .execute(
                mService.getEdit(params),
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

    public void getEdit(String token, String id) {
        RXUtil.execute(mService.getEdit(token, id), new Observer<CompilePleaseBean>() {
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
            public void onNext(CompilePleaseBean compilePleaseBean) {
                if (compilePleaseBean.code == 0) {
                    mView.dataSucceed(compilePleaseBean);
                } else {
                    mView.dataDefeated(compilePleaseBean.msg);
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
