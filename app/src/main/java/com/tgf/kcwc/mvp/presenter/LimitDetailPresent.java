package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.DiscountLimitModel;
import com.tgf.kcwc.mvp.model.FindDiscountService;
import com.tgf.kcwc.mvp.model.GiftPackDetailModel;
import com.tgf.kcwc.mvp.model.LimitDiscountDetailModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.GiftPackDetailView;
import com.tgf.kcwc.mvp.view.LimitDiscountDetailView;
import com.tgf.kcwc.mvp.view.LimitDiscountView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/4/26 0026
 * E-mail:hekescott@qq.com
 */

public class LimitDetailPresent extends WrapPresenter<LimitDiscountDetailView> {


    private LimitDiscountDetailView mView;
    private FindDiscountService mService;

    @Override
    public void attachView(LimitDiscountDetailView view) {
        mView = view;
        mService = ServiceFactory.getFindDiscountService();
    }
    public void getDiscountLimitDetail(int id){
        RXUtil.execute(mService.getDiscountLimitDetail(id), new Observer<ResponseMessage<LimitDiscountDetailModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<LimitDiscountDetailModel> giftPackDetailModelResponseMessage) {
                LimitDiscountDetailModel model = giftPackDetailModelResponseMessage.getData();
                mView.showHead(model);
                mView.showGoodsList(model.giftCars);

            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
}
