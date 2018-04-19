package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.ContinueBean;
import com.tgf.kcwc.mvp.model.SignInService;
import com.tgf.kcwc.mvp.view.ContinueView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ContinuePresenter extends WrapPresenter<ContinueView> {

    private ContinueView mView;
    private SignInService mService;

    @Override
    public void attachView(ContinueView view) {
        mView = view;
        mService = ServiceFactory.getSignInService();
    }

    public void getMergeLineDetail(String token, String id) {
        RXUtil.execute(mService.getMergeLineDetail(token, id), new Observer<ContinueBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.detailsDataFeated("列表请求失败");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ContinueBean continueBean) {
                if (continueBean.code == 0) {
                    mView.DetailsSucceed(continueBean);
                } else {
                    mView.detailsDataFeated(continueBean.msg);
                }

            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void getMergeLine(String token, String json) {
        RXUtil.execute(mService.getMergeLine(token, json), new Observer<BaseBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.detailsDataFeated("列表请求失败");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(BaseBean continueBean) {
                if (continueBean.code == 0) {
                    mView.DetaSucceed(continueBean);
                } else {
                    mView.detailsDataFeated(continueBean.msg);
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
