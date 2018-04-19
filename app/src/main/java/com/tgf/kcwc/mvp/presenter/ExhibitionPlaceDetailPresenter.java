package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ExhibitPlace;
import com.tgf.kcwc.mvp.model.ExhibitionService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.ExhibitPlaceDetailView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/1/23 0023
 * E-mail:hekescott@qq.com
 */

public class ExhibitionPlaceDetailPresenter extends WrapPresenter<ExhibitPlaceDetailView> {
    private ExhibitPlaceDetailView mView;
    private ExhibitionService      mService;

    @Override
    public void attachView(ExhibitPlaceDetailView view) {
        mView = view;
        mService = ServiceFactory.getExhibitionService();
    }

    public void getExhibitionPalaceDetail(int eventId, int hallId){
     Subscription subscription = RXUtil.execute(mService.getExhibitPlaceDetail(eventId, hallId), new Observer<ResponseMessage<ExhibitPlace>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<ExhibitPlace> exhibitPlaceResponseMessage) {
                ExhibitPlace exhibitPlace = exhibitPlaceResponseMessage.getData();
                mView.showHeadImg(exhibitPlace);
            }
        });

        mSubscriptions.add(subscription);
    }
}
