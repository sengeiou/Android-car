package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.TopicDetailsBean;
import com.tgf.kcwc.mvp.model.TopicDetailsListBean;
import com.tgf.kcwc.mvp.model.TopicListService;
import com.tgf.kcwc.mvp.view.CompileTopicDetailsView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class CompileTopicDetailsPresenter extends WrapPresenter<CompileTopicDetailsView> {

    private CompileTopicDetailsView mView;
    private TopicListService mService;

    @Override
    public void attachView(CompileTopicDetailsView view) {
        mView=view;
        mService= ServiceFactory.getTopicListService();
    }

    /**
     * 获取话题信息
     * @param token
     * @param topicId
     */
    public void getTopicDetail(String token,String topicId){
        RXUtil.execute(mService.getTopicDetail(token,topicId), new Observer<TopicDetailsBean>() {
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
            public void onNext(TopicDetailsBean topicBean) {
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
     * 获取话题列表
     * @param token
     * @param topicId
     * @param page
     */
    public void GetTopicListData(String token,String topicId,int page){
        RXUtil.execute(mService.GetTopicListData(token,topicId,page), new Observer<TopicDetailsListBean>() {
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
            public void onNext(TopicDetailsListBean topicDetailsListBean) {
                if (topicDetailsListBean.code==0){
                    mView.dataListSucceed(topicDetailsListBean);
                }else {
                    mView.dataListDefeated(topicDetailsListBean.msg);
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
     * 编辑
     * @param token
     */
    public void GetUpdate(String token,String cover,String id,String intro){
        RXUtil.execute(mService.GetUpdate(token,cover,id,intro), new Observer<BaseArryBean>() {
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
            public void onNext(BaseArryBean topicDetailsListBean) {
                if (topicDetailsListBean.code==0){
                    mView.UpdateSucceed(topicDetailsListBean);
                }else {
                    mView.dataListDefeated(topicDetailsListBean.msg);
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
     * 置顶
     * @param token
     */
    public void GetTop(String token,String thread_id,String topic_id){
        RXUtil.execute(mService.GetTop(token,thread_id,topic_id), new Observer<BaseArryBean>() {
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
            public void onNext(BaseArryBean topicDetailsListBean) {
                if (topicDetailsListBean.code==0){
                    mView.TopSucceed(topicDetailsListBean);
                }else {
                    mView.dataListDefeated(topicDetailsListBean.msg);
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
