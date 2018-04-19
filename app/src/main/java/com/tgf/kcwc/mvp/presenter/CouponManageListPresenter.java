package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CouponManageListModel;
import com.tgf.kcwc.mvp.model.ManageCouponService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.CouponManageListView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/2/14 0014
 * E-mail:hekescott@qq.com
 */

public class CouponManageListPresenter extends WrapPresenter<CouponManageListView>  {

    private  CouponManageListView mView;
    private ManageCouponService mService;
    @Override
    public void attachView(CouponManageListView view) {
        mView = view;
        mService = ServiceFactory.getManageCouponService();
    }

    public void getCouponManageList(String token){
      Subscription subscription = RXUtil.execute(mService.getCouponManageList(token), new Observer<ResponseMessage<CouponManageListModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<CouponManageListModel> couponManageListModelResponseMessage) {
                mView.showCouponManageList(couponManageListModelResponseMessage.getData().list);
                if(couponManageListModelResponseMessage.statusCode!=0){
                    CommonUtils.showToast(mView.getContext(),couponManageListModelResponseMessage.statusMessage);
                }
            }
        });

        mSubscriptions.add(subscription);
    }
}
