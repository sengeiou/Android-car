package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.CouponService;
import com.tgf.kcwc.mvp.model.ExclusiveCoupon;
import com.tgf.kcwc.mvp.model.MyCouponModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TransactionModel;
import com.tgf.kcwc.mvp.view.CouponView;
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

public class CouponPresenter extends WrapPresenter<CouponView> {
    private CouponView    mView;
    private Subscription  mSubscription;
    private CouponService mService = null;

    @Override
    public void attachView(CouponView view) {
        this.mView = view;
        mService = ServiceFactory.getCouponService();
    }

    /**
     *
     * 加载代金券数据
     *
     */
    public void loadCoupons(String token, String isExpiration) {

        mSubscription = RXUtil.execute(mService.getMyCoupons(token, isExpiration),
            new Observer<ResponseMessage<List<Coupon>>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {

                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<List<Coupon>> responseMessage) {

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
     * 加载代金券数据
     *
     */
    public void loadCoupons(String token, String isExpiration, int page, int pageSize) {

        mSubscription = RXUtil.execute(mService.getMyCoupons(token, isExpiration, page, pageSize), new Observer<ResponseMessage<MyCouponModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(ResponseMessage<MyCouponModel> myCouponModelResponseMessage) {
                            mView.showMyCouponList(myCouponModelResponseMessage.getData().list);
                        mView.shoMyCouponCount(myCouponModelResponseMessage.getData().pagination);
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

    /**
     *
     * 获取领取/购买代金券的必要参数
     *
     */
    public void getCouponPayData(String couponId, String distributeListId, String payType,
                                 String num, String token) {

        mSubscription = RXUtil.execute(
            mService.payCouponOrder(couponId, distributeListId, payType, num, token),
            new Observer<ResponseMessage<TransactionModel>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {

                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<TransactionModel> responseMessage) {
                    if(responseMessage.statusCode==0){
                        mView.showDatas(responseMessage.data);
                    }else  if(responseMessage.statusCode==20001){
                        mView.shwoFailed(responseMessage.statusMessage);
                    }else{
                        mView.shwoFailed(responseMessage.statusMessage);
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
     *
     * 获取专属代金券列表
     *
     */
    public void getExCoupons(String token, String isExpiration) {

        mSubscription = RXUtil.execute(mService.getExclusiveCoupons(token, isExpiration),
            new Observer<ResponseMessage<List<ExclusiveCoupon>>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {

                    mView.showLoadingTasksError();
                }
                @Override
                public void onNext(ResponseMessage<List<ExclusiveCoupon>> responseMessage) {

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

}
