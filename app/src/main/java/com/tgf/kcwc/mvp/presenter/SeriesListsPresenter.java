package com.tgf.kcwc.mvp.presenter;

import com.google.gson.Gson;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.SeriesByBrandModel;
import com.tgf.kcwc.mvp.model.StoreBrand;
import com.tgf.kcwc.mvp.model.VehicleService;
import com.tgf.kcwc.mvp.view.BrandDataView;
import com.tgf.kcwc.mvp.view.SeriesDataView;
import com.tgf.kcwc.see.SeriesDetailActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;
import com.tgf.kcwc.util.SharedPreferenceUtil;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/14 15:55
 * E-mail：fishloveqin@gmail.com
 * 根据品牌获取车系列表
 */

public class SeriesListsPresenter extends WrapPresenter<SeriesDataView> {
    private SeriesDataView mView;
    private Subscription mSubscription;
    private VehicleService mService = null;

    @Override
    public void attachView(SeriesDataView view) {
        this.mView = view;
        mService = ServiceFactory.getVehicleService();
    }


    /**
     * 获取车系列表
     */
    public void getSeriesDatas(String brandId) {

        mSubscription = RXUtil.execute(mService.getSeriesByBrand(brandId),
                new Observer<ResponseMessage<SeriesByBrandModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<SeriesByBrandModel> responseMessage) {

                        if (Constants.NetworkStatusCode.SUCCESS == responseMessage.statusCode) {
                            mView.loadDatas(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
                        }

                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                });

        mSubscriptions.add(mSubscription);
    }

}
