package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.PleaseService;
import com.tgf.kcwc.mvp.view.PleaseDetailsView;

/**
 * Created by Administrator on 2017/4/18.
 */

public class PleaseDetailsDataPrenter extends WrapPresenter<PleaseDetailsView> {

    private PleaseDetailsView mView;
    private PleaseService mService;

    @Override
    public void attachView(PleaseDetailsView view) {
        mView = view;
        mService = ServiceFactory.getPleaseService();
    }

    public void getListData(String token, int page,String begin_time,String brand_id,String type,String city) {
/*        RXUtil.execute(mService.getPleaseList(token, page + "",begin_time,brand_id,	type,city), new Observer<PleasePlayModel>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("列表请求失败");
            }

            @Override
            public void onNext(PleasePlayModel pleasePlayModel) {
                mView.dataListSucceed(pleasePlayModel);
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });*/
    }


}
