package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ExhibitionService;
import com.tgf.kcwc.mvp.model.PopmanEslistModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.PopmanEsListView;
import com.tgf.kcwc.util.RXUtil;

import retrofit2.http.Query;
import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/4/6 0006
 * E-mail:hekescott@qq.com
 */

public class PopmanEsListPresenter  extends WrapPresenter<PopmanEsListView>{

    private PopmanEsListView mView;
    private ExhibitionService mService;
    @Override
    public void attachView(PopmanEsListView view) {
        mService = ServiceFactory.getExhibitionService();
        mView = view;
    }
    public void getPopmanesList(String brandId, String pricMax, String priceMin, Integer seatNum, int mPageIndex,int mPageSize){
        RXUtil.execute(mService.getDarenLists(brandId, pricMax, priceMin, seatNum,mPageIndex,mPageSize), new Observer<ResponseMessage<PopmanEslistModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.showLoadingTasksError();
            }

            @Override
            public void onNext(ResponseMessage<PopmanEslistModel> popmanEslistModelResponseMessage) {
                if (popmanEslistModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showPopmanEsList(popmanEslistModelResponseMessage.getData().popmanModelist);
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
