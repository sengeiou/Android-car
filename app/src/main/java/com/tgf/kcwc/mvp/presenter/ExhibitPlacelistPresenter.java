package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ExhibitPlaceListModel;
import com.tgf.kcwc.mvp.model.ExhibitionService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.ExhibitPlacelistView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/1/16 0016
 * E-mail:hekescott@qq.com
 */

public class ExhibitPlacelistPresenter implements BasePresenter<ExhibitPlacelistView> {
    private ExhibitPlacelistView mView;
    private ExhibitionService mService;
    private Subscription mSubscription;

    @Override
    public void attachView(ExhibitPlacelistView view) {
        mView = view;
        mService = ServiceFactory.getExhibitionService();
    }

    @Override
    public void detachView() {
        unsubscribe();
    }
    public void getExhibitPlacelist(int exhitbitId,String token){
        mSubscription = RXUtil.execute(mService.getExhibitionPlaceList(exhitbitId, token), new Observer<ResponseMessage<ExhibitPlaceListModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<ExhibitPlaceListModel> exhibitPlaceListModelResponseMessage) {
                ExhibitPlaceListModel exhibitPlaceListModel=   exhibitPlaceListModelResponseMessage.getData();
                if (exhibitPlaceListModel!=null){
                    mView.showHeadView(exhibitPlaceListModel.totalImg);
                    mView.showGridView(exhibitPlaceListModel.exhibitPlaceList);
                }else {
                    mView.showLoadingTasksError();
                }
            }
        });

    }


    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

    }
}
