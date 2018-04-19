package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ExhibitEvent;
import com.tgf.kcwc.mvp.model.ExhibitionService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.ExhibitEventDetailView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/1/22 0022
 * E-mail:hekescott@qq.com
 */

public class ExhibitEventDetailPresenter extends WrapPresenter<ExhibitEventDetailView> {
    private ExhibitEventDetailView mView;
    private ExhibitionService      mService;

    @Override
    public void attachView(ExhibitEventDetailView view) {
        mView = view;
        mService = ServiceFactory.getExhibitionService();
    }

    public  void  getExhibitionEventDetail(int eventId){
    Subscription subscription= RXUtil.execute(mService.getEventDetail(eventId), new Observer<ResponseMessage<ExhibitEvent>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showLoadingTasksError();
            }

            @Override
            public void onNext(ResponseMessage<ExhibitEvent> exhibitEventResponseMessage) {
                mView.showHead(exhibitEventResponseMessage.getData());
            }
        });

        mSubscriptions.add(subscription);
    }
}
