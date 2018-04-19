package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.AccessRecordModel;
import com.tgf.kcwc.mvp.model.CertDetailModel;
import com.tgf.kcwc.mvp.model.CertListModel;
import com.tgf.kcwc.mvp.model.CertResultModel;
import com.tgf.kcwc.mvp.model.CertifcateService;
import com.tgf.kcwc.mvp.model.OrganizationBean;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CertDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/14 15:55
 * E-mail：fishloveqin@gmail.com
 * 证件列表、详情
 */

public class CertDataPresenter extends WrapPresenter<CertDataView> {
    private CertDataView mView;
    private Subscription mSubscription;
    private CertifcateService mService = null;

    @Override
    public void attachView(CertDataView view) {
        this.mView = view;
        mService = ServiceFactory.getCertService();
    }

    /**
     * 加载证件列表数据
     */
    public void loadCertListDatas(String token) {

        mSubscription = RXUtil.execute(mService.getCertLists(token),
                new Observer<ResponseMessage<CertListModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<CertListModel> responseMessage) {

                        mView.showData(responseMessage.data);
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
     * 加载证件列表数据
     */
    public void loadCertDetailDatas(String isNeedReceive

            , String id, String isNeedTotal, String code, String token) {

        mSubscription = RXUtil.execute(
                mService.getCertDetail(isNeedReceive, id, isNeedTotal, code, token),
                new Observer<ResponseMessage<CertDetailModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<CertDetailModel> responseMessage) {

                        mView.showData(responseMessage.data);
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
     * 加载证件列表数据
     */
    public void loadCertPrintDatas(String id, String token) {

        mSubscription = RXUtil.execute(mService.getPrintData(id, token),
                new Observer<ResponseMessage<CertResultModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<CertResultModel> responseMessage) {

                        mView.showData(responseMessage.data);
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
     * 证件挂失
     */
    public void applyCertLoss(String id, String token) {

        mSubscription = RXUtil.execute(mService.applyCertLoss(id, token),
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

                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            mView.showData(responseMessage.statusMessage);
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
     * 门禁记录
     */
    public void loadAccessRecords(String id, String token) {

        mSubscription = RXUtil.execute(mService.getAccessRecords(id, token),
                new Observer<ResponseMessage<AccessRecordModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<AccessRecordModel> responseMessage) {

                        mView.showData(responseMessage.data);
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
     * 证件须知
     */
    public void loadCertDesc(String id) {

        mSubscription = RXUtil.execute(mService.getCertDesc(id),
                new Observer<ResponseMessage<CertResultModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<CertResultModel> responseMessage) {

                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            mView.showData(responseMessage.data);
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
     * 证件申领状态
     */
    public void getCertStatus(String cid, String eventId, String qdId, String token) {

        mSubscription = RXUtil.execute(mService.getCertStatus(cid, eventId, qdId, token),
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

                        DataItem dataItem = new DataItem();
                        dataItem.code = responseMessage.statusCode;
                        dataItem.msg = responseMessage.statusMessage;
                        dataItem.obj = responseMessage.data;
                        System.out.println("obj:" + responseMessage.data);
                        mView.showData(dataItem);
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
     * 关联证件信息
     */
    public void relationshipCertInfo(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.commitCertRelationshipForms(params),
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

                        DataItem dataItem = new DataItem();
                        dataItem.code = responseMessage.statusCode;
                        dataItem.msg = responseMessage.statusMessage;
                        mView.showData(dataItem);
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
     * 关联证件信息
     */
    public void getOrgList(String keyword) {

        mSubscription = RXUtil.execute(mService.getOrgList(keyword),
                new Observer<ResponseMessage<List<OrganizationBean>>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<OrganizationBean>> responseMessage) {

                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            mView.showData(responseMessage.data);
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
