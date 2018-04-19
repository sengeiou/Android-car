package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ExhibitionEventPicsModel;
import com.tgf.kcwc.mvp.model.ExhibitionService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.ExhibitEventView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/1/19 0019
 * E-mail:hekescott@qq.com
 */

public class ExhibitEventPicsPresenter extends WrapPresenter<ExhibitEventView> {
    private ExhibitEventView  mView;
    private ExhibitionService mService;

    @Override
    public void attachView(ExhibitEventView view) {
        mView = view;
        mService = ServiceFactory.getExhibitionService();
    }

    public void getExhibitPics(int eventId, int pageSize, int page) {
        Subscription subscription = RXUtil.execute(
                mService.getActivityPhotoStore(eventId, pageSize, page),
                new Observer<ResponseMessage<ExhibitionEventPicsModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<ExhibitionEventPicsModel> exhibitionEventPicsModelResponseMessage) {
                        mView.showHead(exhibitionEventPicsModelResponseMessage.getData());
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                });
        mSubscriptions.add(subscription);
    }

}
