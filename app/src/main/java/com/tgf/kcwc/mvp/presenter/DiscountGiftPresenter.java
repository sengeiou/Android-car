package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.DiscountGiftModel;
import com.tgf.kcwc.mvp.model.DiscountLimitModel;
import com.tgf.kcwc.mvp.model.FindDiscountService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.DiscountGiftView;
import com.tgf.kcwc.mvp.view.LimitDiscountView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/4/25 0025
 * E-mail:hekescott@qq.com
 */

public class DiscountGiftPresenter implements BasePresenter<DiscountGiftView> {

    private DiscountGiftView mView;
    private FindDiscountService mService;
    @Override
    public void attachView(DiscountGiftView view) {
        mView = view;
        mService = ServiceFactory.getFindDiscountService();
    }
    public void getLimitDiscountList(Integer cityid, Integer endPrice, Integer factoryId,Integer order, Integer special,Integer startPrice){
        RXUtil.execute(mService.getDiscountGiftList(cityid, endPrice, factoryId, order, 1, special, startPrice), new Observer<ResponseMessage<DiscountGiftModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<DiscountGiftModel> limitModelResponseMessage) {
                mView.showLimitList(limitModelResponseMessage.getData().list);
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(false);
            }
        });
    }



    @Override
    public void detachView() {

    }
}
