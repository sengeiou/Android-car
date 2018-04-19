package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.VehicleService;
import com.tgf.kcwc.mvp.view.SetCustomizationView;
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
 * 新车发布
 */

public class SetCustomizationPresenter extends WrapPresenter<SetCustomizationView> {
    private SetCustomizationView mView;
    private Subscription         mSubscription;
    private VehicleService       mService = null;

    @Override
    public void attachView(SetCustomizationView view) {
        this.mView = view;
        mService = ServiceFactory.getVehicleService();
    }

    /**
     *  
     * 获取品牌列表
     * 
     */
    public void setCustomizationData(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.setCustomizationInfo(params),
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
                public void onNext(ResponseMessage<List<String>> responseBodyResponseMessage) {

                    if (responseBodyResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                        mView.setSuccess();
                    } else {
                        CommonUtils.showToast(mView.getContext(),
                            responseBodyResponseMessage.statusMessage);
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
