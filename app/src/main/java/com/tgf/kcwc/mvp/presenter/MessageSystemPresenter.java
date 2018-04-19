package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.MessageListBean;
import com.tgf.kcwc.mvp.model.MessageListService;
import com.tgf.kcwc.mvp.model.MessagePrivateListBean;
import com.tgf.kcwc.mvp.model.MessageSystemListBean;
import com.tgf.kcwc.mvp.view.MessageListView;
import com.tgf.kcwc.mvp.view.MessageSystemListView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class MessageSystemPresenter extends WrapPresenter<MessageSystemListView> {

    private MessageSystemListView mView;
    private MessageListService mService;

    @Override
    public void attachView(MessageSystemListView view) {
        mView = view;
        mService = ServiceFactory.getMessageListService();
    }

    public void getAppStatistics(String token, String type, int page,int event_id) {
        RXUtil.execute(mService.getStatisticsList(token, type, page,event_id), new Observer<MessageSystemListBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
            }

            @Override
            public void onNext(MessageSystemListBean messageListBean) {
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

    public void getLetterlistList(String token, String letteruser, int page) {
        RXUtil.execute(mService.getLetterlistList(token, letteruser, page), new Observer<MessagePrivateListBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
            }

            @Override
            public void onNext(MessagePrivateListBean messageListBean) {
                if (messageListBean.code == 0) {
                    mView.PrivateListSucceed(messageListBean);
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

    public void getDelmessage(String token, String letteruser) {
        RXUtil.execute(mService.getDelmessage(token, letteruser), new Observer<BaseArryBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
            }

            @Override
            public void onNext(BaseArryBean baseBean) {
                if (baseBean.code == 0) {
                    mView.DeleteSucceed(baseBean);
                } else {
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
