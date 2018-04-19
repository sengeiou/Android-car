package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CouponDetailModel;
import com.tgf.kcwc.mvp.model.CouponService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CouponDetailView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;

/**
 * Auther: Scott
 * Date: 2017/1/12 0012
 * E-mail:hekescott@qq.com
 */

public class CouponDetailPresenter implements BasePresenter<CouponDetailView> {
    private CouponDetailView mView;
    private CouponService mService;

    @Override
    public void attachView(CouponDetailView view) {
        mView = view;
        mService = ServiceFactory.getCouponService();
    }

    @Override
    public void detachView() {

    }

    public void getCouponDetail(int id, String lat, String lon, String token) {

        RXUtil.execute(mService.getCouponDetail(id, lat, lon, token), new Observer<ResponseMessage<CouponDetailModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<CouponDetailModel> couponDetailModelResponseMessage) {
                if (couponDetailModelResponseMessage.getData() != null)
                    mView.showHead(couponDetailModelResponseMessage.getData());
            }
        });

    }
}
