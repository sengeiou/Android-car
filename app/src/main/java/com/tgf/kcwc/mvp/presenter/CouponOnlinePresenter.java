package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.CouponService;
import com.tgf.kcwc.mvp.model.ExclusiveCoupon;
import com.tgf.kcwc.mvp.model.MyCouponModel;
import com.tgf.kcwc.mvp.model.OnlineCoupon;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TransactionModel;
import com.tgf.kcwc.mvp.view.CouponOnlineView;
import com.tgf.kcwc.mvp.view.CouponView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/14 15:55
 * E-mail：fishloveqin@gmail.com
 * 代金券
 */

public class CouponOnlinePresenter extends WrapPresenter<CouponOnlineView> {
    private CouponOnlineView mView;
    private Subscription mSubscription;
    private CouponService mService = null;

    @Override
    public void attachView(CouponOnlineView view) {
        this.mView = view;
        mService = ServiceFactory.getCouponService();
    }


    /**
     * 加载代金券数据
     * Integer categoryId,
     @Query("brandId")Integer brandId,@Query("distanceId") Integer distanceId
     */
    public void loadOnlineCoupons(String token,  int page, int pageSize,Integer categoryId,
                            Integer brandId, Integer distanceId ,
                             Integer orderId) {

        mSubscription = RXUtil.execute(mService.getOnlineCoupon(token, page, pageSize,categoryId,brandId,distanceId,orderId), new Observer<ResponseMessage<OnlineCoupon>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(ResponseMessage<OnlineCoupon> onlineCouponResponseMessage) {
                        if (onlineCouponResponseMessage.statusCode == 0) {
                            mView.showSuccess(onlineCouponResponseMessage.getData().onlineCouponList);
                        } else {
                            CommonUtils.showToast(mView.getContext(), onlineCouponResponseMessage.statusMessage);
                        }
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                }
        );

        mSubscriptions.add(mSubscription);
    }


}
