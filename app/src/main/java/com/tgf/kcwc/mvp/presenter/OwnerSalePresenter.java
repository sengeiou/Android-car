package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.ChatMessageBean;
import com.tgf.kcwc.mvp.model.GoodsEditModel;
import com.tgf.kcwc.mvp.model.OwnerSaleEditModel;
import com.tgf.kcwc.mvp.model.OwnerSaleModel;
import com.tgf.kcwc.mvp.model.OwnerSaleService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.SaleDetailModel;
import com.tgf.kcwc.mvp.model.SaleListModel;
import com.tgf.kcwc.mvp.model.SaleMgrModel;
import com.tgf.kcwc.mvp.view.OwnerSaleDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2017/1/17 14:21
 * E-mail：fishloveqin@gmail.com
 */

public class OwnerSalePresenter extends WrapPresenter<OwnerSaleDataView> {

    private OwnerSaleDataView mView;
    private Subscription mSubscription;
    private OwnerSaleService mService = null;

    public void attachView(OwnerSaleDataView view) {
        this.mView = view;
        mService = ServiceFactory.getSaleService();
    }

    /**
     * 获取车主自售列表
     */
    public void loadOwnerSalesList(String areaId, String brandId, String seriesId, String maxPrice,
                                   String minPrice, String sort) {

        mSubscription = RXUtil.execute(
                mService.getSaleList(areaId, brandId, seriesId, maxPrice, minPrice, sort),
                new Observer<ResponseMessage<SaleListModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<SaleListModel> repData) {

                        mView.showData(repData.data);
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
     * 获取车主自售列表
     */
    public void loadOwnerSalesList(Map<String, String> params) {

        mSubscription = RXUtil.execute(
                mService.getSaleList(params),
                new Observer<ResponseMessage<SaleListModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<SaleListModel> repData) {

                        mView.showData(repData.data);
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
     * 获取车主自售详情
     */
    public void loadOwnerSalesDetail(String id, String token) {

        mSubscription = RXUtil.execute(mService.getSaleDetail(id, token),
                new Observer<ResponseMessage<SaleDetailModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<SaleDetailModel> repData) {

                        mView.showData(repData.data);
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
     * 发布车主自售
     */
    public void publishOwnerGoods(Map<String, String> params, int eventId) {

        mSubscription = RXUtil.execute(mService.publishOwnerSaleGoods(params, eventId),
                new Observer<OwnerSaleModel>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(OwnerSaleModel repData) {
//
//                        DataItem dataItem = new DataItem();
//                        dataItem.code = repData.statusCode;
//                        dataItem.msg = repData.statusMessage;
                        mView.showData(repData);
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
     * 发布车主自售
     */
    public void publishOwnerSaleEditGoods(Map<String, String> params, int eventId, int threadId) {

        mSubscription = RXUtil.execute(mService.publishOwnerSaleEditGoods(params, eventId, threadId),
                new Observer<OwnerSaleEditModel>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setLoadingIndicator(false);
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(OwnerSaleEditModel repData) {
//
//                        DataItem dataItem = new DataItem();
//                        dataItem.code = repData.statusCode;
//                        dataItem.msg = repData.statusMessage;
                        mView.showData(repData);
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
     * 我的自售列表
     */
    public void saleMgrList(String status, String token) {

        mSubscription = RXUtil.execute(mService.getSaleMgrList(status, token),
                new Observer<ResponseMessage<SaleMgrModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<SaleMgrModel> repData) {
                        int code = repData.statusCode;
                        if (Constants.NetworkStatusCode.SUCCESS == code) {
                            mView.showData(repData.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), "" + repData.statusMessage);
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
     * 下架
     */
    public void unShelve(String id, String token) {

        mSubscription = RXUtil.execute(mService.unShelve(id, token),
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
                    public void onNext(ResponseMessage<List<String>> repData) {
                        int code = repData.statusCode;
                        if (Constants.NetworkStatusCode.SUCCESS == code) {
                            mView.showData(repData.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), "" + repData.statusMessage);
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
     * 下架
     */
    public void getGoodsEditInfo(String id) {

        mSubscription = RXUtil.execute(mService.goodsEditInfo(id),
                new Observer<ResponseMessage<GoodsEditModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<GoodsEditModel> repData) {
                        int code = repData.statusCode;
                        if (Constants.NetworkStatusCode.SUCCESS == code) {
                            mView.showData(repData.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), "" + repData.statusMessage);
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
     * 获取联系人列表
     */
    public void getContactsList(String id, String token) {

        mSubscription = RXUtil.execute(mService.getContactList(id, token),
                new Observer<ResponseMessage<List<Account.UserInfo>>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<Account.UserInfo>> repData) {
                        int code = repData.statusCode;
                        if (Constants.NetworkStatusCode.SUCCESS == code) {
                            mView.showData(repData.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), "" + repData.statusMessage);
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
     * 获取聊天会话数据
     */
    public void getMsgDatas(String id, String userId, String token) {

        mSubscription = RXUtil.execute(mService.getMsgDatas(id, userId, token),
                new Observer<ResponseMessage<List<ChatMessageBean>>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<ChatMessageBean>> repData) {
                        int code = repData.statusCode;
                        if (Constants.NetworkStatusCode.SUCCESS == code) {
                            mView.showData(repData.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), "" + repData.statusMessage);
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
