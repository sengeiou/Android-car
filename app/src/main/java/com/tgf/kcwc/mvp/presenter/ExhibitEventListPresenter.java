package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.EventListModel;
import com.tgf.kcwc.mvp.model.ExhibitEvent;
import com.tgf.kcwc.mvp.model.ExhibitionService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.EventListView;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/1/18 0018
 * E-mail:hekescott@qq.com
 */

public class ExhibitEventListPresenter extends WrapPresenter<EventListView> {
    private EventListView     mView;
    private ExhibitionService mService;

    @Override
    public void attachView(EventListView view) {
        mView = view;
        mService = ServiceFactory.getExhibitionService();
    }

   public void getEventList(final int eventId, int type, int pageSize, int page){
       Subscription subscription =  RXUtil.execute(mService.getEventlist(eventId, type, pageSize, page), new Observer<ResponseMessage<EventListModel>>() {
           @Override
           public void onCompleted() {
               mView.setLoadingIndicator(false);
           }

           @Override
           public void onError(Throwable e) {
               mView.showLoadingTasksError();
           }

           @Override
           public void onNext(ResponseMessage<EventListModel> eventListModelResponseMessage) {
               EventListModel eventListModel = eventListModelResponseMessage.getData();
               List<ExhibitEvent> eventList = eventListModel.exhibitEventlist;
               if (eventList != null && eventList.size() != 0) {
                   mView.showEventList(eventList);
               } else {
                   mView.showNomore("没有数据了");
               }
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
