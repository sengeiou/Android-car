package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.PleaseFoundListBean;
import com.tgf.kcwc.mvp.model.PleaseListService;
import com.tgf.kcwc.mvp.view.PleaseFoundListView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class PleaseFoundListPresenter extends WrapPresenter<PleaseFoundListView> {

    private PleaseFoundListView mView;
    private PleaseListService mService;

    @Override
    public void attachView(PleaseFoundListView view) {
        mView=view;
        mService= ServiceFactory.getPleaseListService();
    }

    public void gainFoundLsis(String token,int page){
        RXUtil.execute(mService.getCreateList(token,page), new Observer<PleaseFoundListBean>() {
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
            public void onNext(PleaseFoundListBean foundListBean) {
                if (foundListBean.code==0){
                    mView.dataListSucceed(foundListBean);
                }else {
                    mView.dataListDefeated(foundListBean.msg);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
    public void getActivityCancel(String token,String id){
        RXUtil.execute(mService.getActivityCancel(token,id), new Observer<BaseBean>() {
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
