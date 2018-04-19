package com.tgf.kcwc.mvp.presenter;

import android.util.Log;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BusinessAttentionService;
import com.tgf.kcwc.mvp.model.FriendGroupingModel;
import com.tgf.kcwc.mvp.model.FriendListModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.BusinessAttentionView;
import com.tgf.kcwc.mvp.view.WrapView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/8/7 14:51
 */

public class BusinessAttentionPresenter extends WrapPresenter<BusinessAttentionView> {
    private BusinessAttentionService mService;
    private BusinessAttentionView mView;
    private Subscription mSubscription;

    @Override
    public void attachView(BusinessAttentionView view) {
        this.mView = view;
        mService = ServiceFactory.getAttentionService();
    }
    /**
     * 获取好友全部列表
     */
    public void getFriendAllList(String token,int page) {
        mSubscription = RXUtil.execute(mService.getFriendAllList(token,page), new Observer<ResponseMessage<FriendListModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<FriendListModel> friendListModelResponseMessage) {
                if (friendListModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showFriendList(friendListModelResponseMessage.data.list);
                } else {
                    CommonUtils.showToast(mView.getContext(),
                            friendListModelResponseMessage.statusMessage);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
        mSubscriptions.add(mSubscription);
    }
    /**
     * 获取搜索好友列表
     */
    public void getSearchFriendList(String token, String name,int page) {
        mSubscription = RXUtil.execute(mService.getSearchFriendList(token, name,page), new Observer<ResponseMessage<FriendListModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<FriendListModel> friendListModelResponseMessage) {
                if (friendListModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showFriendList(friendListModelResponseMessage.data.list);
                } else {
                    CommonUtils.showToast(mView.getContext(),
                            friendListModelResponseMessage.statusMessage);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
        mSubscriptions.add(mSubscription);
    }
    /**
     * 获取分组好友列表
     */
    public void getGroupingFriendList(String token, int friendGroupId,int page) {
        mSubscription = RXUtil.execute(mService.getGroupingFriendList(token, friendGroupId,page), new Observer<ResponseMessage<FriendListModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<FriendListModel> friendListModelResponseMessage) {
                if (friendListModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showFriendList(friendListModelResponseMessage.data.list);
                } else {
                    CommonUtils.showToast(mView.getContext(),
                            friendListModelResponseMessage.statusMessage);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
        mSubscriptions.add(mSubscription);
    }

    /**
     * 获取分组列表
     */
    public void getGroupingList(String token) {
        mSubscription = RXUtil.execute(mService.getFriendGrouping(token), new Observer<FriendGroupingModel>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(FriendGroupingModel friendGroupingModel) {
                mView.setLoadingIndicator(false);
                mView.showGrouping(friendGroupingModel.list);
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
        mSubscriptions.add(mSubscription);
    }
}
