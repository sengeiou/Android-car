package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.AttentionDataModel;
import com.tgf.kcwc.mvp.model.HobbyTag;
import com.tgf.kcwc.mvp.model.MyFavoriteDataModel;
import com.tgf.kcwc.mvp.model.PersonHomeDataModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.UserBaseDataModel;
import com.tgf.kcwc.mvp.model.UserDetailDataModel;
import com.tgf.kcwc.mvp.model.UserDynamicModel;
import com.tgf.kcwc.mvp.model.UserHomeDataModel;
import com.tgf.kcwc.mvp.model.UserManagerService;
import com.tgf.kcwc.mvp.model.UserRideDataModel;
import com.tgf.kcwc.mvp.view.UserDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:20175/15 13:55
 * E-mail：fishloveqin@gmail.com
 * 会员中心-首页
 */

public class UserDataPresenter extends WrapPresenter<UserDataView> {
    private UserDataView mView;
    private Subscription mSubscription;
    private UserManagerService mService = null;

    @Override
    public void attachView(UserDataView view) {
        this.mView = view;
        mService = ServiceFactory.getUMService();
    }

    /**
     * 会员中心-首页
     */
    public void loadUserHomeInfo(String token, String type) {

        mSubscription = RXUtil.execute(mService.getUserHomeInfo(token, type),
                new Observer<ResponseMessage<UserHomeDataModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<UserHomeDataModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                            UserHomeDataModel userHomeDataModel = responseMessage.data;
                            mView.showDatas(responseMessage.data);

                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
     * 发送短信
     */
    public void sendSMS(String type, String tel, String token) {

        mSubscription = RXUtil.execute(mService.sendSMS(type, tel, token),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
     * 获取用户资料详细信息
     *
     * @param userId
     * @param token
     */
    public void getPersonalDetailData(String userId, String token) {

        mSubscription = RXUtil.execute(mService.getPersonalDetailData(userId, token),
                new Observer<ResponseMessage<PersonHomeDataModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<PersonHomeDataModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
     * 获取用户资料基本信息
     *
     * @param userId
     * @param token
     */
    public void getPersonalBaseData(String userId, String token) {

        mSubscription = RXUtil.execute(mService.getPersonalBaseData(userId, token),
                new Observer<ResponseMessage<UserBaseDataModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<UserBaseDataModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
     * 获取用户关注列表
     *
     * @param userId
     * @param token
     */
    public void getAttentionList(String userId, String token) {

        mSubscription = RXUtil.execute(mService.getAttentionList(userId, token),
                new Observer<ResponseMessage<AttentionDataModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<AttentionDataModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
     * 获取用户粉丝列表
     *
     * @param userId
     * @param token
     */
    public void getFansList(String userId, String token) {

        mSubscription = RXUtil.execute(mService.getFansList(userId, token),
                new Observer<ResponseMessage<AttentionDataModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<AttentionDataModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
     * 获取用户更多关注列表
     *
     * @param userId
     * @param token
     */
    public void getMoreAttentionList(String userId, String token) {

        mSubscription = RXUtil.execute(mService.getAttentionMoreList(userId, token),
                new Observer<ResponseMessage<List<AttentionDataModel.UserInfo>>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<AttentionDataModel.UserInfo>> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
     * 获取用户动态信息
     *
     * @param userId
     * @param token
     */
    public void getDynamicInfo(String userId, String token) {

        mSubscription = RXUtil.execute(mService.getDynamicInfo(userId, token),
                new Observer<ResponseMessage<UserDynamicModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<UserDynamicModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
     * 获取用户详情
     *
     * @param userId
     * @param token
     */
    public void getUserDetailInfo(String userId, String token) {

        mSubscription = RXUtil.execute(mService.getUserDetailInfo(userId, token),
                new Observer<ResponseMessage<UserDetailDataModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<UserDetailDataModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
     * 获取兴趣标签
     *
     * @param token
     */
    public void getHobbyTags(String token) {

        mSubscription = RXUtil.execute(mService.getHobbyTags(token),
                new Observer<ResponseMessage<List<HobbyTag>>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<HobbyTag>> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
     * 修改用户资料
     *
     * @param params
     */
    public void updateUserInfo(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.updateUserInfo(params),
                new Observer<ResponseMessage<Account.UserInfo>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Account.UserInfo> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
     * 获取我的收藏数据
     *
     * @param token
     */
    public void getFavoriteData(String model, String page, String pageSize, String token) {

        mSubscription = RXUtil.execute(mService.getMyFavoriteData(model, page, pageSize, token),
                new Observer<ResponseMessage<MyFavoriteDataModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<MyFavoriteDataModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
     * 删除收藏
     *
     * @param params
     */
    public void deleteFavoriteData(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.deleteFavoriteData(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
     * 添加收藏
     *
     * @param params
     */
    public void addFavoriteData(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.addFavoriteData(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
     * 取消收藏
     *
     * @param params
     */
    public void cancelFavoriteData(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.cancelFavoriteData(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
     * 获取用户详情
     *
     * @param userId
     * @param token
     */
    public void getRideDataById(String userId, String token) {

        mSubscription = RXUtil.execute(mService.getRideData(userId),
                new Observer<ResponseMessage<UserRideDataModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<UserRideDataModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
     * 修改用户资料
     *
     * @param nickname
     */
    public void checkNicknameIsExist(String nickname, String token) {

        mSubscription = RXUtil.execute(mService.checkNicknameIsExist(nickname, token),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                            mView.showDatas(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
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
