package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.DynamicDetailsBean;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.Topic;
import com.tgf.kcwc.mvp.model.TopicDetailModel;
import com.tgf.kcwc.mvp.model.TopicSearchModel;
import com.tgf.kcwc.mvp.model.VehicleService;
import com.tgf.kcwc.mvp.view.TopicDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/22 16:55
 * E-mail：fishloveqin@gmail.com
 * 帖子Presenter
 */

public class TopicDataPresenter extends WrapPresenter<TopicDataView> {
    private TopicDataView mView;
    private Subscription mSubscription;
    private VehicleService mService = null;

    public void attachView(TopicDataView view) {
        this.mView = view;
        mService = ServiceFactory.getVehicleService();
    }

    /**
     * 获取帖子列表
     *
     * @param id
     * @param type 1为店铺 2为车主自售
     */
    public void loadPlayerTopics(String id, String type) {

        mSubscription = RXUtil.execute(mService.playerTopics(id),
                new Observer<ResponseMessage<List<Topic>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseMessage<List<Topic>> repData) {

                        mView.showData(repData.data);
                    }
                });
        mSubscriptions.add(mSubscription);
    }

    /**
     * 获取帖子列表
     */
    public void loadTopicsListV2(String title, String token) {

        mSubscription = RXUtil.execute(mService.getTopicListV2(title, token),
                new Observer<ResponseMessage<TopicSearchModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseMessage<TopicSearchModel> repData) {

                        if (Constants.NetworkStatusCode.SUCCESS == repData.statusCode) {
                            mView.showData(repData.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), repData.statusMessage);
                        }

                    }
                });
        mSubscriptions.add(mSubscription);
    }


    /**
     * 获取帖子列表
     */
    public void loadTopicsList(String title, String token) {

        mSubscription = RXUtil.execute(mService.getTopicList(title, token),
                new Observer<ResponseMessage<List<Topic>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseMessage<List<Topic>> repData) {

                        mView.showData(repData.data);
                    }
                });
        mSubscriptions.add(mSubscription);
    }

    /**
     * 帖子编辑
     *
     * @params
     */
    public void editTopic(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.editTopic(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseMessage<Object> repData) {

                        if (Constants.NetworkStatusCode.SUCCESS == repData.statusCode) {
                            mView.showData(repData.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), repData.statusMessage + "");
                        }

                    }
                });
        mSubscriptions.add(mSubscription);
    }


    /**
     * 发帖
     *
     * @params
     */
    public void createNewTopic(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.createTopic(params),
                new Observer<ResponseMessage<List<String>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseMessage<List<String>> repData) {

                        if (Constants.NetworkStatusCode.SUCCESS == repData.statusCode) {
                            mView.showData(repData.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), repData.statusMessage + "");
                        }

                    }
                });
        mSubscriptions.add(mSubscription);
    }


    /**
     * 获取帖子详情
     *
     * @param id
     */
    public void getTopicDetails(String id, String longitude, String latitude, String token) {

        mSubscription = RXUtil.execute(mService.getTopicDetail(id, longitude, latitude, token),
                new Observer<ResponseMessage<TopicDetailModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseMessage<TopicDetailModel> repData) {

                        if (Constants.NetworkStatusCode.SUCCESS == repData.statusCode) {
                            mView.showData(repData.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), repData.statusMessage + "");
                        }

                    }
                });
        mSubscriptions.add(mSubscription);
    }

    /**
     * 创建话题、标签
     */
    public void createTag(String title, String token) {

        mSubscription = RXUtil.execute(mService.createTag(title, token),
                new Observer<ResponseMessage<Topic>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseMessage<Topic> repData) {

                        if (Constants.NetworkStatusCode.SUCCESS == repData.statusCode) {
                            mView.showData(repData.data);
                        } else {

                            CommonUtils.showToast(mView.getContext(), "添加失败，请稍候重试!");
                        }

                    }
                });
        mSubscriptions.add(mSubscription);
    }

    /**
     * 创建话题、标签
     */
    public void createTag(String fromId, String fromType, String token) {

        mSubscription = RXUtil.execute(mService.createTag(fromId, fromType, token),
                new Observer<ResponseMessage<Topic>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseMessage<Topic> repData) {

                        if (Constants.NetworkStatusCode.SUCCESS == repData.statusCode) {
                            mView.showData(repData.data);
                        } else {

                            CommonUtils.showToast(mView.getContext(), "查询失败，请稍候重试!");
                        }

                    }
                });
        mSubscriptions.add(mSubscription);
    }


    /**
     * 删除帖子
     *
     * @params
     */
    public void deleteTopic(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.deleteTopic(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseMessage<Object> repData) {

                        if (Constants.NetworkStatusCode.SUCCESS == repData.statusCode) {
                            mView.showData(repData.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), repData.statusMessage + "");
                        }

                    }
                });
        mSubscriptions.add(mSubscription);
    }

    /**
     * 发布动态
     *
     * @params
     */
    public void releasemood(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.releasemood(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> repData) {

                        if (Constants.NetworkStatusCode.SUCCESS == repData.statusCode) {
                            mView.showData(repData.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), repData.statusMessage + "");
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
     * 动态详情
     *
     * @params
     */
    public void detailMood(String token, String thread_id) {

        mSubscription = RXUtil.execute(mService.detailMood(token, thread_id),
                new Observer<ResponseMessage<DynamicDetailsBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(ResponseMessage<DynamicDetailsBean> repData) {
                        if (Constants.NetworkStatusCode.SUCCESS == repData.statusCode) {
                            mView.showData(repData.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), repData.statusMessage + "");
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
