package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CouponFellowModel;
import com.tgf.kcwc.mvp.model.ManageCouponService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CouponFellowView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;

/**
 * Auther: Scott
 * Date: 2017/2/16 0016
 * E-mail:hekescott@qq.com
 */

public class CouponFellowPresenter extends WrapPresenter<CouponFellowView> {
    private CouponFellowView mView;
    private ManageCouponService mService;

    @Override
    public void attachView(CouponFellowView view) {
        mView = view;
        mService = ServiceFactory.getManageCouponService();
    }

    public void getFellowRecord(String token, int couponId, String mobile){
        RXUtil.execute(mService.getCouponTail(token, couponId, mobile), new Observer<ResponseMessage<CouponFellowModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(ResponseMessage<CouponFellowModel> arrayListResponseMessage) {
                        mView.showCouponFellows(arrayListResponseMessage.getData());
            }
        });
    }
}
