package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CouponService;
import com.tgf.kcwc.mvp.model.RefondDetailModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CouponRefondDetailView;
import com.tgf.kcwc.util.RXUtil;

import retrofit2.http.Query;
import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/8/11 0011
 * E-mail:hekescott@qq.com
 */

public class CouponRefondDetailPresenter extends WrapPresenter<CouponRefondDetailView> {
    private CouponRefondDetailView mView;
    private CouponService mService;
    @Override
    public void attachView(CouponRefondDetailView view) {
        mView = view;
        mService = ServiceFactory.getCouponService();
    }

    public void getCouponRefondDetail(String token, int codeId){
        RXUtil.execute(mService.getRefondDetail(token, codeId), new Observer<ResponseMessage<RefondDetailModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<RefondDetailModel> refondDetailModelResponseMessage) {
                RefondDetailModel refondDetailModel =   refondDetailModelResponseMessage.getData();
                mView.showHead(refondDetailModel.coupon);
                mView.showRefondInfo(refondDetailModel);
                mView.showRefondProgress(refondDetailModel.progress_list);

            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
}
