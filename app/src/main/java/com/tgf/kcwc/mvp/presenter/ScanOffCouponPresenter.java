package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CouponService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.ScanOffListModel;
import com.tgf.kcwc.mvp.model.SetttingService;
import com.tgf.kcwc.mvp.view.ScanOffCouponView;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/5/24 0024
 * E-mail:hekescott@qq.com
 */

public class ScanOffCouponPresenter extends WrapPresenter<ScanOffCouponView> {

    private ScanOffCouponView mView;
    private CouponService mCouponService;
    private SetttingService mService;

    @Override
    public void attachView(ScanOffCouponView view) {
        mView = view;
        mCouponService = ServiceFactory.getCouponService();
        mService = ServiceFactory.getSetttingService();
    }
    public void getScanOffList(String token){
        RXUtil.execute(mCouponService.getScanoffList(token), new Observer<ResponseMessage<ScanOffListModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<ScanOffListModel> scanOffListModelResponseMessage) {
                    mView.showScanOffList(scanOffListModelResponseMessage.getData().list);
            }
        });
    }

    public void setCouponOnline(String token) {
        RXUtil.execute(mService.setCouponOnline(token), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                if (responseMessage.statusCode == 0) {
                    mView.showSalerSetSuccess();
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void getCouponOnline(String token) {
        RXUtil.execute(mService.getCouponOnline(token), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage jsonObjectResponseMessage) {
                Map map = (Map) jsonObjectResponseMessage.getData();
                int online = (int) map.get("coupon_online");
                mView.showsIsOnline(online == 1);

            }


        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
}
