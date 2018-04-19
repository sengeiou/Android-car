package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.SalespersonModel;
import com.tgf.kcwc.mvp.model.VehicleService;
import com.tgf.kcwc.mvp.view.SalespersonView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2017/1/17 14:21
 * E-mail：fishloveqin@gmail.com
 */

public class SalespersonPresenter extends WrapPresenter<SalespersonView> {

    private SalespersonView mView;
    private Subscription    mSubscription;
    private VehicleService  mService = null;

    public void attachView(SalespersonView view) {
        this.mView = view;
        mService = ServiceFactory.getVehicleService();
    }

    /**
     *
     *获取销售精英列表
     *@param  orgId
     */
    public void loadSalesperson(String orgId, String token) {

        mSubscription = RXUtil.execute(mService.salespersonLists(orgId, token),
            new Observer<ResponseMessage<SalespersonModel>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {

                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<SalespersonModel> repData) {

                    mView.showSales(repData.data);
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
