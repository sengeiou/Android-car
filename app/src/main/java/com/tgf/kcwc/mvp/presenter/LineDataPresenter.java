package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.LineDataModel;
import com.tgf.kcwc.mvp.model.LineDataService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.LineDataView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/10/16
 * @describle
 */
public class LineDataPresenter extends WrapPresenter<LineDataView> {
    LineDataView mView;
    LineDataService mService;
    Subscription mSubscription;

    @Override
    public void attachView(LineDataView view) {
        mService = ServiceFactory.getLineDataService();
        mView = view;
    }

    /**
     * 获取线路数据
     *
     * @param token
     * @param longitude
     * @param latitude
     * @param bookId
     * @param lat
     * @param lng
     */
    public void getLineData(String token, String longitude, String latitude, int bookId, String lat, String lng) {
        mSubscription = RXUtil.execute(mService.getLineData(token, longitude, latitude, bookId, lat, lng), new Observer<ResponseMessage<LineDataModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<LineDataModel> lineDataModelResponseMessage) {
                if (lineDataModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showLineData(lineDataModelResponseMessage.data);
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
