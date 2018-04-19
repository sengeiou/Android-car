package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CouponSendRecordModel;
import com.tgf.kcwc.mvp.model.ManageCouponService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CouponSendRecordView;
import com.tgf.kcwc.util.RXUtil;

import java.util.ArrayList;

import rx.Observer;

/**
 * Auther: Scott
 * Date: 2017/2/15 0015
 * E-mail:hekescott@qq.com
 */

public class CouponSendRecordPresenter extends WrapPresenter<CouponSendRecordView> {
    private CouponSendRecordView mView;
    private ManageCouponService mService;

    @Override
    public void attachView(CouponSendRecordView view) {
        mView = view;
        mService = ServiceFactory.getManageCouponService();
    }

    public void getCouponSendRecord(String token, int couponId){
        RXUtil.execute(mService.getCouponSendRecord(token, couponId), new Observer<ResponseMessage<ArrayList<CouponSendRecordModel>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<ArrayList<CouponSendRecordModel>> arrayListResponseMessage) {
                    mView.showCouponSendRecord(arrayListResponseMessage.getData());
            }
        });
    }
}
