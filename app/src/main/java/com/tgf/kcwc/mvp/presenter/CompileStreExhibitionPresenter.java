package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.CarColor;
import com.tgf.kcwc.mvp.model.CompileExhibitionBean;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.StoreBelowUpBean;
import com.tgf.kcwc.mvp.model.StoreExhibitionService;
import com.tgf.kcwc.mvp.view.CompileStoreExhibitionView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class CompileStreExhibitionPresenter extends WrapPresenter<CompileStoreExhibitionView> {

    private CompileStoreExhibitionView mView;
    private StoreExhibitionService mService;

    @Override
    public void attachView(CompileStoreExhibitionView view) {
        mView = view;
        mService = ServiceFactory.getStoreExhibitionService();
    }

    public void getDetail(String token, String id) {
        RXUtil.execute(mService.getDetail(token, id), new Observer<CompileExhibitionBean>() {
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
            public void onNext(CompileExhibitionBean baseBean) {
                if (baseBean.code == 0) {
                    mView.gainDataSucceed(baseBean);
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
        RXUtil.execute(mService.getEdit(token, storeBelowUpBean.appearanceColorName,
            storeBelowUpBean.appearanceColorName, storeBelowUpBean.appearanceImg,
            storeBelowUpBean.boothId, storeBelowUpBean.eventId, storeBelowUpBean.factoryId,
            storeBelowUpBean.hallId, storeBelowUpBean.interiorColorName,
            storeBelowUpBean.interiorColorValue, storeBelowUpBean.interiorImg,
            storeBelowUpBean.carId, "", storeBelowUpBean.seriesId,
            storeBelowUpBean.status, storeBelowUpBean.id), new Observer<BaseBean>() {
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
