package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.IntegralOrderDetailBean;
import com.tgf.kcwc.mvp.model.IntegralOrderPayParam;
import com.tgf.kcwc.mvp.model.IntegralService;
import com.tgf.kcwc.mvp.model.OrderPayParam;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.IntegraOrderPaysView;
import com.tgf.kcwc.mvp.view.IntegralOrderDetailView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;

/**
 * Author：Jenny
 * Date:2016/12/14 15:55
 * E-mail：fishloveqin@gmail.com
 * 订单
 */

public class IntegralOrderPaysPresenter extends WrapPresenter<IntegraOrderPaysView> {
    private IntegraOrderPaysView mView;
    private Subscription mSubscription;
    private IntegralService mService = null;

    @Override
    public void attachView(IntegraOrderPaysView view) {
        this.mView = view;
        mService = ServiceFactory.getIntegralService();
    }

    /**
     * 订单是否成功
     *
     * @param id
     * @param token
     */
    public void getIsPayStatus(String token, String id) {

        mSubscription = RXUtil.execute(mService.getIsPayStatus(id, token),
                new Observer<BaseArryBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(BaseArryBean orderModelResponseMessage) {
                        mView.showIntegralOrderDetail(orderModelResponseMessage);
                    }
                });
        mSubscriptions.add(mSubscription);
    }

}
