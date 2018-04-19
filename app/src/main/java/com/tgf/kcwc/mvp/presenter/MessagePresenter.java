package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.AppListBean;
import com.tgf.kcwc.mvp.model.AppListService;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.MessageListBean;
import com.tgf.kcwc.mvp.model.MessageListService;
import com.tgf.kcwc.mvp.view.ApplyListView;
import com.tgf.kcwc.mvp.view.MessageListView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class MessagePresenter extends WrapPresenter<MessageListView> {

    private MessageListView mView;
    private MessageListService mService;

    @Override
    public void attachView(MessageListView view) {
        mView = view;
        mService = ServiceFactory.getMessageListService();
    }


    public void getAppStatistics(String token) {
        RXUtil.execute(mService.getAppStatistics(token), new Observer<MessageListBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
            }

            @Override
            public void onNext(MessageListBean messageListBean) {
                if (messageListBean.code == 0) {
                    mView.StatisticsListSucceed(messageListBean);
                } else {
                    mView.dataListDefeated(messageListBean.msg);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }


    public void getPrivateletterList(String token, int page) {
        RXUtil.execute(mService.getPrivateletterList(token, page), new Observer<MessageListBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
            }

            @Override
            public void onNext(MessageListBean messageListBean) {
                if (messageListBean.code == 0) {
                    mView.PrivateletterListSucceed(messageListBean);
                } else {
                    mView.dataListDefeated(messageListBean.msg);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void getRead(String token) {
        RXUtil.execute(mService.getRead(token), new Observer<BaseBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
            }

            @Override
            public void onNext(BaseBean messageListBean) {
                if (messageListBean.code == 0) {
                    mView.ReadSucceed(messageListBean);
                } else {
                    mView.dataListDefeated(messageListBean.msg);
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
