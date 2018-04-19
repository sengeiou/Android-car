package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CouponService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketCoupon;
import com.tgf.kcwc.mvp.view.CouponDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2017/9/8 11:55
 * E-mail：fishloveqin@gmail.com
 * 代金券
 */

public class CouponDataPresenter extends WrapPresenter<CouponDataView> {
    private CouponDataView mView;
    private Subscription mSubscription;
    private CouponService mService = null;

    @Override
    public void attachView(CouponDataView view) {
        this.mView = view;
        mService = ServiceFactory.getCouponService();
    }

    /**
     * 加载代金券数据
     */
    public void loadCoupons(String eventId, String token) {

        mSubscription = RXUtil.execute(mService.getTicketCoupons(eventId, token),
                new Observer<ResponseMessage<List<TicketCoupon>>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<TicketCoupon>> responseMessage) {

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
     * 领取代金券
     */
    public void getCoupon(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.getCoupon(params),
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
