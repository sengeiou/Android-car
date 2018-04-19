package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.AppListBean;
import com.tgf.kcwc.mvp.model.AppListService;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.SeekBean;
import com.tgf.kcwc.mvp.model.SeekService;
import com.tgf.kcwc.mvp.view.ApplyListView;
import com.tgf.kcwc.mvp.view.SeekView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;

/**
 * Created by Administrator on 2017/4/21.
 */

public class SeekPresenter extends WrapPresenter<SeekView> {

    private SeekView mView;
    private SeekService mService;

    @Override
    public void attachView(SeekView view) {
        mView=view;
        mService= ServiceFactory.getSeekService();
    }

    public void getsSearchList(String token,String name){
        RXUtil.execute(mService.getsSearchList(token,name), new Observer<SeekBean>() {
            @Override
            public void onCompleted() {
                mView.getContext();
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
            }

            @Override
            public void onNext(SeekBean seekBean) {
                if (seekBean.code==0){
                    mView.dataListSucceed(seekBean);
                }else {
                    mView.dataListDefeated(seekBean.msg);
                }
            }
        });
    }

    public void getsSearch(String token,String name){
        RXUtil.execute(mService.getsSearchList(token,name), new Observer<SeekBean>() {
            @Override
            public void onCompleted() {
                mView.getContext();
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
            }

            @Override
            public void onNext(SeekBean seekBean) {
                if (seekBean.code==0){
                    mView.dataSucceed(seekBean);
                }else {
                    mView.dataListDefeated(seekBean.msg);
                }
            }
        });
    }


}
