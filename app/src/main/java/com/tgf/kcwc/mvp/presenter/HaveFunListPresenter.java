package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.HaveFunBean;
import com.tgf.kcwc.mvp.model.TopicListService;
import com.tgf.kcwc.mvp.view.HaveFunListView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class HaveFunListPresenter extends WrapPresenter<HaveFunListView> {

    private HaveFunListView mView;
    private TopicListService mService;

    @Override
    public void attachView(HaveFunListView view) {
        mView=view;
        mService= ServiceFactory.getTopicListService();
    }

    public void GetDiges(String token,String type,int page){
        RXUtil.execute(mService.GetDiges(token,type,page), new Observer<HaveFunBean>() {
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
            public void onNext(HaveFunBean haveFunBean) {
                if (haveFunBean.code==0){
                    mView.dataListSucceed(haveFunBean);
                }else {
                    mView.dataListDefeated(haveFunBean.msg);
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
