package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.DiscountLimitNewModel;
import com.tgf.kcwc.mvp.model.FindDiscountService;
import com.tgf.kcwc.mvp.model.LimitDiscountEveModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.SelectExbitionModel;
import com.tgf.kcwc.mvp.view.LimitNewDiscountView;
import com.tgf.kcwc.mvp.view.SelectExhibitionView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/4/25 0025
 * E-mail:hekescott@qq.com
 */

public class SelectExhibitionPresenter implements BasePresenter<SelectExhibitionView> {

    private SelectExhibitionView mView;
    private FindDiscountService mService;
    @Override
    public void attachView(SelectExhibitionView view) {
        mView = view;
        mService = ServiceFactory.getFindDiscountService();
    }


    public void getEventDataLists(){
        RXUtil.execute(mService.getEventList(), new Observer<ResponseMessage<List<SelectExbitionModel>>>() {
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
            public void onNext(ResponseMessage<List<SelectExbitionModel>> limitModelResponseMessage) {
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
