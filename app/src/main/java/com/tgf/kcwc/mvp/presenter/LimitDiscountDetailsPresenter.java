package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.FindDiscountService;
import com.tgf.kcwc.mvp.model.LimitDiscountNewDetailsLimitModel;
import com.tgf.kcwc.mvp.model.LimitDiscountNewDetailsModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.SelectExbitionModel;
import com.tgf.kcwc.mvp.view.LimitDiscountDetailsView;
import com.tgf.kcwc.mvp.view.SelectExhibitionView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;

import retrofit2.http.Query;
import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/4/25 0025
 * E-mail:hekescott@qq.com
 */

public class LimitDiscountDetailsPresenter implements BasePresenter<LimitDiscountDetailsView> {

    private LimitDiscountDetailsView mView;
    private FindDiscountService mService;
    @Override
    public void attachView(LimitDiscountDetailsView view) {
        mView = view;
        mService = ServiceFactory.getFindDiscountService();
    }


    public void getEventDetail(String benefit_id){
        RXUtil.execute(mService.getEventDetail(benefit_id), new Observer<ResponseMessage<LimitDiscountNewDetailsModel>>() {
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
            public void onNext(ResponseMessage<LimitDiscountNewDetailsModel> limitModelResponseMessage) {
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

    public void getStoreDetail(String benefit_id,double longitude, double latitude){
        RXUtil.execute(mService.getStoreDetail(benefit_id,longitude,latitude), new Observer<ResponseMessage<LimitDiscountNewDetailsLimitModel>>() {
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
            public void onNext(ResponseMessage<LimitDiscountNewDetailsLimitModel> limitModelResponseMessage) {
                if (limitModelResponseMessage.statusCode==0){
                    mView.showStoreLimitList(limitModelResponseMessage.getData());
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
