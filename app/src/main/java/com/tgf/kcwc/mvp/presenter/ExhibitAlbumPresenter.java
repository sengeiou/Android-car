package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ExhibitionPicsModel;
import com.tgf.kcwc.mvp.model.ExhibitionService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.ExhibitionAlbumView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/1/20 0020
 * E-mail:hekescott@qq.com
 */

public class ExhibitAlbumPresenter extends WrapPresenter<ExhibitionAlbumView> {
    private ExhibitionAlbumView mView;
    private ExhibitionService   mService;

    @Override
    public void attachView(ExhibitionAlbumView view) {
        mView = view;
        mService = ServiceFactory.getExhibitionService();
    }

    public void getExhibitionPics(int exhibitId, Integer brand_id, Integer hall_id, Integer is_type,
                                  int pageSize, int page) {
      Subscription subscription = RXUtil.execute(
              mService.getExhibitPhotoStore(exhibitId, brand_id, hall_id, is_type, pageSize, page),
              new Observer<ResponseMessage<ExhibitionPicsModel>>() {
                  @Override
                  public void onCompleted() {
                      mView.setLoadingIndicator(false);
                  }

                  @Override
                  public void onError(Throwable e) {

                  }

                  @Override
                  public void onNext(ResponseMessage<ExhibitionPicsModel> exhibitionPicsModelResponseMessage) {
                      mView.showExhbitionPics(
                              exhibitionPicsModelResponseMessage.getData().exhibitImgeList);
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
