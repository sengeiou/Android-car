package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ApplyListBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.DrivingService;
import com.tgf.kcwc.mvp.model.RelationBean;
import com.tgf.kcwc.mvp.view.ApplyView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class ApplyListPresenter extends WrapPresenter<ApplyView> {

    private ApplyView mView;
    private DrivingService mService;
    @Override
    public void attachView(ApplyView view) {
        mView=view;
        mService = ServiceFactory.getDrivingService();
    }

    public void gainAppLsis( String token,String id,int page){
        RXUtil.execute(mService.getApplylist(token,id,page), new Observer<ApplyListBean>() {
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
            public void onNext(ApplyListBean applyListBean) {
                if (applyListBean.data.list.size()!=0&&applyListBean.data.list!=null){
                    mView.dataListSucceed(applyListBean);
                }else {
                    mView.dataListDefeated("没有数据了");
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void getFollow(String token, final String follow_id, final int num){
        RXUtil.execute(mService.getFollow(token,follow_id), new Observer<BaseBean>() {
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
            public void onNext(BaseBean attentionBean) {
                if (attentionBean.code==0){
                    mView.dataFollowSucceed(num,follow_id,"关注成功");
                }else {
                    mView.dataListDefeated("关注失败");
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
    public void getCancel(String token, final String follow_id, final int num){
        RXUtil.execute(mService.getCancel(token,follow_id), new Observer<BaseBean>() {
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
            public void onNext(BaseBean attentionBean) {
                if (attentionBean.code==0){
                    mView.dataFollowSucceed(num,follow_id,"取消关注成功");
                }else {
                    mView.dataListDefeated("取消关注失败");
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void getFollowRelation(String token, String follow_id, final int num){
        RXUtil.execute(mService.getFollowRelation(token,follow_id), new Observer<RelationBean>() {
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
            public void onNext(RelationBean attentionBean) {
                if (attentionBean.code==0){
                    mView.dataFollowRelationSucceed(num,attentionBean.data.relation);
                }else {
                    mView.dataListDefeated(attentionBean.msg);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void getCheck(String token, String id, final int status, final int num){
        RXUtil.execute(mService.getCheck(token,id,status), new Observer<BaseBean>() {
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
            public void onNext(BaseBean attentionBean) {
                if (attentionBean.code==0){
                    mView.dataCheckSucceed(num,status);
                }else {
                    mView.dataListDefeated(attentionBean.msg);
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
