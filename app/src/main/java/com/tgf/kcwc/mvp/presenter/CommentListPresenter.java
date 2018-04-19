package com.tgf.kcwc.mvp.presenter;

import android.view.View;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CommentModel;
import com.tgf.kcwc.mvp.model.CommentService;
import com.tgf.kcwc.mvp.model.LikeBean;
import com.tgf.kcwc.mvp.model.LikeListModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CommentListView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/14 15:55
 * E-mail：fishloveqin@gmail.com
 * 帖子讨论、评价Presenter
 */

public class CommentListPresenter extends WrapPresenter<CommentListView> {
    private CommentListView mView;
    private Subscription    mSubscription;
    private CommentService  mService = null;

    @Override
    public void attachView(CommentListView view) {
        this.mView = view;
        mService = ServiceFactory.getCommentService();
    }

    /**
     * 获取评价列表
     * @param module
     * @param resId
     * @param vehicleType
     */
    public void loadEvaluateList(String module, final String resId, String vehicleType) {

        mSubscription = RXUtil.execute(mService.getEvaluateList(module, resId, vehicleType),
            new Observer<ResponseMessage<CommentModel>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {

                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<CommentModel> responseMessage) {
                    if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                        mView.showDatas(responseMessage.data);
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
     * 获取评论列表
     * @param module
     * @param resId
     * @param vehicleType
     */
    public void loadCommentList(String module, final String resId, String vehicleType) {

        mSubscription = RXUtil.execute(mService.getCommentList(module, resId, vehicleType, IOUtils.getToken(mView.getContext())),
            new Observer<ResponseMessage<CommentModel>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {

                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<CommentModel> responseMessage) {
                    if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                        mView.showDatas(responseMessage.data);
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
     * 获取评论列表
     * @param module
     * @param resId
     * @param vehicleType
     */
    public void loadCommentList(String module, final String resId, String vehicleType, int page,
                                int pageSize) {

        mSubscription = RXUtil.execute(
            mService.getCommentList(module, resId, vehicleType, page, pageSize),
            new Observer<ResponseMessage<CommentModel>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {

                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<CommentModel> responseMessage) {
                    if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                        mView.showDatas(responseMessage.data);
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
     * 获取评论详情
     * @param id
     */
    public void loadCommentDetail(String id) {

        mSubscription = RXUtil.execute(mService.getCommentDeatail(id,IOUtils.getToken(mView.getContext())),
            new Observer<ResponseMessage<CommentModel>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {

                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<CommentModel> responseMessage) {
                    if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                        mView.showDatas(responseMessage.data);
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
     * 获取点赞列表
     *@param  id
     */
    public void loadLikeList(String id, String token) {

        mSubscription = RXUtil.execute(mService.getLikeList(id, token),
            new Observer<ResponseMessage<LikeListModel>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {

                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<LikeListModel> responseMessage) {
                    if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                        mView.showDatas(responseMessage.data);
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

    List<String> list = new ArrayList<>();

    /**
     * 点赞
     *@param  id
     */
    public void executePraise(String id, String type, String token) {

        mSubscription = RXUtil.execute(mService.executePraise(id, type, token),
            new Observer<ResponseMessage<LikeBean>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {

                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<LikeBean> responseMessage) {
                    if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                        mView.showDatas(responseMessage.data);
                    } else {
                        CommonUtils.showToast(mView.getContext(),
                            "" + responseMessage.statusMessage);
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
     * 点赞
     *@param  id
     */
    public void executePraise(String id, String type, final View v, String token) {

        mSubscription = RXUtil.execute(mService.executePraise(id, type, token),
            new Observer<ResponseMessage<LikeBean>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {

                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<LikeBean> responseMessage) {
                    if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                        mView.showDatas(v);
                    } else {
                        CommonUtils.showToast(mView.getContext(),
                            "" + responseMessage.statusMessage);
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
     * 发布评论
     *@param  params 
     */
    public void publishCmt(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.publishCmt(params),
            new Observer<ResponseMessage<List<String>>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {

                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<List<String>> responseMessage) {
                    if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                        mView.showDatas(responseMessage.data);
                    } else {
                        CommonUtils.showToast(mView.getContext(),
                            "" + responseMessage.statusMessage);
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
}
