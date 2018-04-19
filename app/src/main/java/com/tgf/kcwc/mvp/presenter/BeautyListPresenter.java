package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.Beauty;
import com.tgf.kcwc.mvp.model.BeautyListModel;
import com.tgf.kcwc.mvp.model.ExhibitionService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.BeautyListView;
import com.tgf.kcwc.util.RXUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Query;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/1/17 0017
 * E-mail:hekescott@qq.com
 */

public class BeautyListPresenter extends WrapPresenter<BeautyListView> {
    private BeautyListView    mView;
    private ExhibitionService mService;
    private Subscription mSubscription;
    private ArrayList<Beauty> enBeautyList = new ArrayList<>();
    @Override
    public void attachView(BeautyListView view) {
        mView = view;
        mService = ServiceFactory.getExhibitionService();
    }

    public void getBeautylist(int exhibitionId, int hallId, int factoryId, int pageSize,int page  ) {
      mSubscription =  RXUtil.execute(mService.getBeautyList(exhibitionId, hallId,factoryId,  pageSize,  page),
              new Observer<ResponseMessage<BeautyListModel>>() {
                  @Override
                  public void onCompleted() {
                      mView.setLoadingIndicator(false);
                  }

                  @Override
                  public void onError(Throwable e) {
                      mView.showLoadingTasksError();
                  }

                  @Override
                  public void onNext(ResponseMessage<BeautyListModel> beautyListModelResponseMessage) {
                      BeautyListModel beautyListModel = beautyListModelResponseMessage.getData();
                      BeautyListModel.Data data = beautyListModel.data;
                      enBeautyList.clear();
                      if (data.beautyBranList != null) {
                          mView.showBeautylistView(data.beautyBranList);
                      }
                      mView.showExhibitNameView(data.exhibitName);

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
