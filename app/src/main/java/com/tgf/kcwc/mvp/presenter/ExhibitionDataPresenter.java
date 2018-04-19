package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CarLaunchModel;
import com.tgf.kcwc.mvp.model.Comment;
import com.tgf.kcwc.mvp.model.ExhibitionService;
import com.tgf.kcwc.mvp.model.Hall;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.VehicleService;
import com.tgf.kcwc.mvp.view.CarLaunchView;
import com.tgf.kcwc.mvp.view.ExhibitionDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/14 15:55
 * E-mail：fishloveqin@gmail.com
 * 新车发布
 */

public class ExhibitionDataPresenter extends WrapPresenter<ExhibitionDataView> {
    private ExhibitionDataView mView;
    private Subscription       mSubscription;
    private ExhibitionService  mService = null;

    @Override
    public void attachView(ExhibitionDataView view) {
        this.mView = view;
        mService = ServiceFactory.getExhibitionService();
    }

    /**
     *  
     * 获取展馆区域列表
     * 
     */
    public void getHallLists(String exhibitionId) {

        mSubscription = RXUtil.execute(mService.getHallLists(exhibitionId),
            new Observer<ResponseMessage<List<Hall>>>() {
                @Override
                public void onCompleted() {
                    mView.setLoadingIndicator(false);
                }

                @Override
                public void onError(Throwable e) {

                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<List<Hall>> responseMessage) {

                    if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                        mView.showData(responseMessage.data);
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
