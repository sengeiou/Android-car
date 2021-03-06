package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.CarColor;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.StoreBelowService;
import com.tgf.kcwc.mvp.model.StoreBelowUpBean;
import com.tgf.kcwc.mvp.view.SponsorStoreBelowView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class SponsorStreBelowPresenter extends WrapPresenter<SponsorStoreBelowView> {

    private SponsorStoreBelowView mView;
    private StoreBelowService     mService;

    @Override
    public void attachView(SponsorStoreBelowView view) {
        mView = view;
        mService = ServiceFactory.getStoreBelowService();
    }

    public void getCarCategoryColors(String token, String type, String color_type) {
        RXUtil.execute(mService.getCarCategoryColors(token, type, color_type),
            new Observer<ResponseMessage<List<CarColor>>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {
                    mView.dataListDefeated("网络异常，稍候再试！");
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onNext(ResponseMessage<List<CarColor>> storeBelowBean) {
                    if (storeBelowBean.statusCode == 0) {
                        mView.dataColourSucceed(storeBelowBean.data);
                    } else {
                        CommonUtils.showToast(mView.getContext(), storeBelowBean.statusMessage);
                    }
                }
            }, new Action0() {
                @Override
                public void call() {
                    mView.setLoadingIndicator(true);
                }
            });
    }

    public void getCreate(String token, StoreBelowUpBean storeBelowUpBean) {
        RXUtil.execute(mService.getCreate(token, storeBelowUpBean.appearanceColorName,
            storeBelowUpBean.appearanceColorName, storeBelowUpBean.appearanceImg,
            storeBelowUpBean.carId, "", storeBelowUpBean.diffConf,
            storeBelowUpBean.factoryId, storeBelowUpBean.interiorColorName,
            storeBelowUpBean.interiorColorValue, storeBelowUpBean.interiorImg,
            storeBelowUpBean.seriesId, storeBelowUpBean.status, storeBelowUpBean.typeExist,
            storeBelowUpBean.typeShow), new Observer<BaseBean>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {
                    mView.dataListDefeated("网络异常，稍候再试！");
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onNext(BaseBean baseBean) {
                    if (baseBean.code == 0) {
                        mView.dataSucceed(baseBean);
                    } else {
                        CommonUtils.showToast(mView.getContext(), baseBean.msg);
                    }
                }
            }, new Action0() {
                @Override
                public void call() {
                    mView.setLoadingIndicator(true);
                }
            });
    }

}
