package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CertResultModel;
import com.tgf.kcwc.mvp.model.CertifcateService;
import com.tgf.kcwc.mvp.model.CheckinTypeModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CheckinTypeView;
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
 * 观众登记
 */

public class CheckinPresenter extends WrapPresenter<CheckinTypeView> {
    private CheckinTypeView   mView;
    private Subscription      mSubscription;
    private CertifcateService mService = null;

    @Override
    public void attachView(CheckinTypeView view) {
        this.mView = view;
        mService = ServiceFactory.getCertService();
    }

    /**
     *
     * 加载表单数据
     *
     */
    public void loadFormsDatas(String senseId, String type, String channelId, String id) {

        mSubscription = RXUtil.execute(mService.professionalCertList(senseId, type, channelId, id),
            new Observer<ResponseMessage<CheckinTypeModel>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {

                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<CheckinTypeModel> responseMessage) {

                    mView.showDatas(responseMessage.data);
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
     *
     * 加载表单备注描述
     *
     */
    public void loadFormsDesc(String id) {

        mSubscription = RXUtil.execute(mService.getCertTypeDesc(id),
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

                    mView.showCertDesc(responseMessage.data);
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
     *
     * 提交表单数据
     *
     */
    public void commitFormsDatas(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.commitForms(params),
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

                    mView.showCommitResult(responseMessage.statusCode,
                        responseMessage.statusMessage);
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
