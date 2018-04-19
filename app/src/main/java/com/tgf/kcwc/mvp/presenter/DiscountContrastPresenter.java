package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.DiscountContrastModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.VehicleService;
import com.tgf.kcwc.mvp.view.DiscountContrastView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2017/8.8
 * E-mail：fishloveqin@gmail.com
 * 优惠对比Presenter
 */

public class DiscountContrastPresenter extends WrapPresenter<DiscountContrastView> {
    private DiscountContrastView mView;
    private Subscription mSubscription;
    private VehicleService mService = null;

    @Override
    public void attachView(DiscountContrastView view) {
        this.mView = view;
        mService = ServiceFactory.getVehicleService();
    }

    /**
     * 获取优惠对比数据
     */
    public void loadContrastList(String carId) {

        mSubscription = RXUtil.execute(mService.getDiscountContrastResult(carId),
                new Observer<ResponseMessage<DiscountContrastModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(ResponseMessage<DiscountContrastModel> responseMessage) {

                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            mView.showContrastView(responseMessage.data);
                        }else {
                            CommonUtils.showToast(mView.getContext(),"暂无对比数据！");
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
