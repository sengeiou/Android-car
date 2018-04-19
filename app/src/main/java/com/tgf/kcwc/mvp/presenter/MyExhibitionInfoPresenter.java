package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.MyExhibitionInfoModel;
import com.tgf.kcwc.mvp.model.MyExhibitionInfoService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.MyExhibitionInfoView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/10/9
 * @describle
 */
public class MyExhibitionInfoPresenter extends WrapPresenter<MyExhibitionInfoView> {
    MyExhibitionInfoService mService;
    MyExhibitionInfoView mView;
    Subscription mSubscription;

    @Override
    public void attachView(MyExhibitionInfoView view) {
        mView = view;
        mService = ServiceFactory.getMyExhibitionInfoService();
    }

    /**
     * 获取展会信息
     *
     * @param token
     * @param applyId
     */
    public void getInfo(String token, int applyId) {
        mSubscription = RXUtil.execute(mService.getInfo(token, applyId), new Observer<ResponseMessage<MyExhibitionInfoModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<MyExhibitionInfoModel> myExhibitionInfoModelResponseMessage) {
                if (myExhibitionInfoModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                    mView.showInfo(myExhibitionInfoModelResponseMessage.data);
                } else {
                    CommonUtils.showToast(mView.getContext(), myExhibitionInfoModelResponseMessage.statusMessage);
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
