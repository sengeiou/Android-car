package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.PleaseAppBean;
import com.tgf.kcwc.mvp.model.PleaseListService;
import com.tgf.kcwc.mvp.view.PleaseApplyListView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class PleaseAppListPresenter extends WrapPresenter<PleaseApplyListView> {

    private PleaseApplyListView mView;
    private PleaseListService mService;

    @Override
    public void attachView(PleaseApplyListView view) {
        mView = view;
        mService = ServiceFactory.getPleaseListService();
    }

    public void gainAppLsis(String token, int page) {
        RXUtil.execute(mService.getAppList(token, page), new Observer<PleaseAppBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(PleaseAppBean foundListBean) {
                mView.dataListSucceed(foundListBean);
            }
        });
    }

    public void getActivityCancel(String token, String id) {
        RXUtil.execute(mService.getCancel(token, id), new Observer<BaseBean>() {
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
                if (baseBean.code == 0) {
                    mView.dataSucceed(baseBean);
                } else {
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
