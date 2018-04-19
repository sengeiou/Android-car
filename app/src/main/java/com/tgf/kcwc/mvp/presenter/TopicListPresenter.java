package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.TopicBean;
import com.tgf.kcwc.mvp.model.TopicListService;
import com.tgf.kcwc.mvp.view.TopicListView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class TopicListPresenter extends WrapPresenter<TopicListView> {

    private TopicListView mView;
    private TopicListService mService;

    @Override
    public void attachView(TopicListView view) {
        mView=view;
        mService= ServiceFactory.getTopicListService();
    }

    public void getTopicList(String token,String type,int page){
        RXUtil.execute(mService.getTopicList(token,type,page), new Observer<TopicBean>() {
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
            public void onNext(TopicBean topicBean) {
                if (topicBean.code==0){
                    mView.dataListSucceed(topicBean);
                }else if (topicBean.code==10001){
                    mView.dataListDefeated("-1");
                }else {
                    mView.dataListDefeated(topicBean.msg);
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
