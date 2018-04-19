package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.GeneralizationService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.SMSDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.EncryptUtil;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;


/**
 * Author：Jenny
 * Date:2016/12/8 20:55
 * E-mail：fishloveqin@gmail.com
 */

public class SMSPresenter extends WrapPresenter<SMSDataView> {
    private SMSDataView mView;
    private Subscription mSubscription;
    private GeneralizationService mService = null;

    private Account mModel;

    @Override
    public void attachView(SMSDataView view) {
        this.mView = view;
        mService = ServiceFactory.getGeneralizationService();
    }


    /**
     * 发送短信
     */
    public void sendSMS(String tel) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String randonStr = EncryptUtil.getRandomString(10);
        RXUtil.execute(mService.sendSMS(tel, timeStamp, randonStr, EncryptUtil.getEncyptStr(randonStr, tel + timeStamp),"change_pay_password"), new Observer<ResponseMessage<Object>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<Object> dataMsg) {


                if (dataMsg.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.loadData(dataMsg.data);
                } else {
                    CommonUtils.showToast(mView.getContext(), dataMsg.statusMessage);
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
