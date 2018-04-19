package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.FindDiscountService;
import com.tgf.kcwc.mvp.model.DiscountLimitModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.LimitDiscountView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/4/25 0025
 * E-mail:hekescott@qq.com
 */

public class DiscountLimitPresenter implements BasePresenter<LimitDiscountView> {

    private LimitDiscountView mView;
    private FindDiscountService mService;
    @Override
    public void attachView(LimitDiscountView view) {
        mView = view;
        mService = ServiceFactory.getFindDiscountService();
    }

    public void getLimitDiscountList(Integer eventId,Integer cityid, Integer endPrice, Integer factoryId,Integer order, Integer special,Integer startPrice,String seriesId,String carId,int page,int pagesiez){
        RXUtil.execute(mService.getDiscountLimitList(eventId,cityid, endPrice, factoryId, order, 0, special, startPrice,seriesId,carId,page,pagesiez), new Observer<ResponseMessage<DiscountLimitModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
                mView.showLimitListError();
            }

            @Override
            public void onNext(ResponseMessage<DiscountLimitModel> limitModelResponseMessage) {
                mView.showLimitList(limitModelResponseMessage.getData().mLimitDiscountItemList);
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    @Override
    public void detachView() {

    }
}
