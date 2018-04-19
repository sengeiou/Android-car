package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.TopicDetailsBean;
import com.tgf.kcwc.mvp.model.TopicDetailsListBean;
import com.tgf.kcwc.mvp.model.TopicListService;
import com.tgf.kcwc.mvp.view.TopicDetailsView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;

/**
 * Created by Administrator on 2017/4/21.
 */

public class TopicDetailsPresenter extends WrapPresenter<TopicDetailsView> {

    private TopicDetailsView mView;
    private TopicListService mService;

    @Override
    public void attachView(TopicDetailsView view) {
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
        });
    }

    /**
     * 关注
     * @param token
     * @param topicId
     */
    public void GetTopicAttention(String token,String topicId){
        RXUtil.execute(mService.GetTopicAttention(token,topicId), new Observer<BaseArryBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(BaseArryBean baseBean) {
                if (baseBean.code==0){
                    mView.Attention(baseBean);
                }else {
                    mView.dataListDefeated(baseBean.msg);
                }
            }
        });
    }

    /**
     * 取消关注
     * @param token
     * @param topicId
     */
    public void GetTopicAttentionDelete(String token,String topicId){
        RXUtil.execute(mService.GetTopicAttentionDelete(token,topicId), new Observer<BaseArryBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(BaseArryBean baseBean) {
                if (baseBean.code==0){
                    mView.AttentionDelete(baseBean);
                }else {
                    mView.dataListDefeated(baseBean.msg);
                }
            }
        });
    }
    /**
     * 申请成为主持人
     * @param token
     * @param topicId
     */
    public void GetApplyHost(String token,String topicId){
        RXUtil.execute(mService.GetApplyHost(token,topicId), new Observer<BaseArryBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(BaseArryBean baseBean) {
                if (baseBean.code==0){
                    mView.ApplyHostSucceed(baseBean);
                }else {
                    mView.dataListDefeated(baseBean.msg);
                }
            }
        });
    }


}
