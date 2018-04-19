package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.FoundTypeBean;
import com.tgf.kcwc.mvp.model.TopicListService;
import com.tgf.kcwc.mvp.view.FoundTopicView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class FoundTopicPresenter extends WrapPresenter<FoundTopicView> {

    private FoundTopicView mView;
    private TopicListService mService;

    @Override
    public void attachView(FoundTopicView view) {
        mView=view;
        mService= ServiceFactory.getTopicListService();
    }

    /**
     * 创建话题
     * @param token
     */
    public void GetCreate(String token,String category_id,String cover,String intro,String title,String category_type){
        RXUtil.execute(mService.GetCreate(token,category_id,cover,intro,title,category_type), new Observer<BaseBean>() {
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
            public void onNext(BaseBean topicBean) {
                if (topicBean.code==0){
                    mView.dataSucceed(topicBean);
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

    /**
     * 获取分类
     * @param token
     */
    public void GetType(String token,String parent_id){
        RXUtil.execute(mService.GetType(token,parent_id), new Observer<FoundTypeBean>() {
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
            public void onNext(FoundTypeBean topicBean) {
                if (topicBean.code==0){
                    mView.typeListSucceed(topicBean);
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

    /**
     * 获取r分类
     * @param token
     */
    public void GetTyper(String token,String parent_id){
        RXUtil.execute(mService.GetType(token,parent_id), new Observer<FoundTypeBean>() {
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
            public void onNext(FoundTypeBean topicBean) {
                if (topicBean.code==0){
                    mView.typerListSucceed(topicBean);
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
