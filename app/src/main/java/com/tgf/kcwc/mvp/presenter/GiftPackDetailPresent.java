package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.FindDiscountService;
import com.tgf.kcwc.mvp.model.GiftPackDetailModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.GiftPackDetailView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/4/26 0026
 * E-mail:hekescott@qq.com
 */

public class GiftPackDetailPresent extends WrapPresenter<GiftPackDetailView> {


    private GiftPackDetailView mView;
    private FindDiscountService mService;

    @Override
    public void attachView(GiftPackDetailView view) {
        mView = view;
        mService = ServiceFactory.getFindDiscountService();
    }
    public void getGiftDetail(int id){
        RXUtil.execute(mService.getGiftPackDetail(id), new Observer<ResponseMessage<GiftPackDetailModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<GiftPackDetailModel> giftPackDetailModelResponseMessage) {
                GiftPackDetailModel model = giftPackDetailModelResponseMessage.getData();
                mView.showHead(model);
                mView.showGoodsList(model.giftCars);
                mView.showOrgList(model.giftOrgs);

            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
}
