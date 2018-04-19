package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.AppListBean;
import com.tgf.kcwc.mvp.model.AppListService;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.view.ApplyListView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class AppListPresenter extends WrapPresenter<ApplyListView> {

    private ApplyListView mView;
    private AppListService mService;

    @Override
    public void attachView(ApplyListView view) {
        mView=view;
        mService= ServiceFactory.getAppListService();
    }

    public void gainAppLsis(String token,int page){
        RXUtil.execute(mService.getAppList(token,page), new Observer<AppListBean>() {
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
            public void onNext(AppListBean appListBean) {
                if (appListBean.code==0){
                    mView.dataListSucceed(appListBean);
                }else {
                    mView.dataListDefeated(appListBean.msg);
                }
            }
        });
    }
    public void getActivityCancel(String token,String id){
        RXUtil.execute(mService.getCancel(token,id), new Observer<BaseBean>() {
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
                    mView.dataSucceed(baseBean);
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
