package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.DiscountLimitNewModel;
import com.tgf.kcwc.mvp.model.FindDiscountService;
import com.tgf.kcwc.mvp.model.LimitDiscountEveModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.LimitNewDiscountView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/4/25 0025
 * E-mail:hekescott@qq.com
 */

public class DiscountLimitNewPresenter implements BasePresenter<LimitNewDiscountView> {

    private LimitNewDiscountView mView;
    private FindDiscountService mService;
    @Override
    public void attachView(LimitNewDiscountView view) {
        mView = view;
        mService = ServiceFactory.getFindDiscountService();
    }

    public void getDataLists(String  seat_num_range_key,String brand_id, String price_range_max, String price_range_min,int page,String city_id){
        RXUtil.execute(mService.getDataLists(seat_num_range_key,brand_id, price_range_max, price_range_min, page,city_id), new Observer<ResponseMessage<DiscountLimitNewModel>>() {
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
            public void onNext(ResponseMessage<DiscountLimitNewModel> limitModelResponseMessage) {
                if (limitModelResponseMessage.statusCode==0){
                    mView.showLimitList(limitModelResponseMessage.getData().mLimitDiscountItemList);
                }else {
                    CommonUtils.showToast(mView.getContext(),limitModelResponseMessage.statusMessage);
                }

            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }


    public void getEventDataLists(String  seat_num_range_key,int page,String factory_id){
        RXUtil.execute(mService.getEventDataLists(seat_num_range_key,page,factory_id), new Observer<ResponseMessage<LimitDiscountEveModel>>() {
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
            public void onNext(ResponseMessage<LimitDiscountEveModel> limitModelResponseMessage) {
                if (limitModelResponseMessage.statusCode==0){
                    mView.showEveLimitList(limitModelResponseMessage.getData());
                }else {
                    CommonUtils.showToast(mView.getContext(),limitModelResponseMessage.statusMessage);
                }

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
