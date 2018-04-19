package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ExhibitionListModel;
import com.tgf.kcwc.mvp.model.ExhibitionService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.ExhibitionListView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;

/**
 * Auther: Scott
 * Date: 2017/1/13 0013
 * E-mail:hekescott@qq.com
 */

public class ExhibitionListPresenter implements BasePresenter<ExhibitionListView> {
    private ExhibitionListView mView;
    private ExhibitionService  mService;

    @Override
    public void attachView(ExhibitionListView view) {
        mView = view;
        mService = ServiceFactory.getExhibitionService();
    }

    @Override
    public void detachView() {

    }

    public void getExhibitionList(String areaId, String page, String pagecount, String token,String adcode) {
        RXUtil.execute(mService.getExhibitionList("22", page, pagecount, token,adcode), new Observer<ResponseMessage<ExhibitionListModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showLoadingTasksError();
            }
            @Override
            public void onNext(ResponseMessage<ExhibitionListModel> exhibitionListModelResponseMessage) {
                ExhibitionListModel exhibitionListModeL = exhibitionListModelResponseMessage.data;

                mView.showExhibitionList(exhibitionListModeL.exhibitionList);
            }
        });

    }

}
