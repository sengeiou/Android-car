package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ExhibitSeeBannerModel;
import com.tgf.kcwc.mvp.model.ExhibitionDetailModel;
import com.tgf.kcwc.mvp.model.ExhibitionListModel;
import com.tgf.kcwc.mvp.model.ExhibitionService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.ExhibitDetailView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/1/16 0016
 * E-mail:hekescott@qq.com
 */

public class ExhibitionDetailPrenter extends WrapPresenter<ExhibitDetailView> {
    private ExhibitDetailView mView;
    private Subscription mSubscription;
    private ExhibitionService mService;

    @Override
    public void attachView(ExhibitDetailView view) {
        mView = view;
        mService = ServiceFactory.getExhibitionService();

    }

    @Override
    public void detachView() {
        unsubscribe();
    }

    public void getExhibitionDetail(int exhibitId, String serverCityId, String adcode) {


        RXUtil.execute(mService.getExhibitionDetail(exhibitId, serverCityId, adcode), new Observer<ResponseMessage<ExhibitionDetailModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showLoadingTasksError();
            }

            @Override
            public void onNext(ResponseMessage<ExhibitionDetailModel> exhibitionDetailModelResponseMessage) {
                ExhibitionDetailModel exhibitionDetailModel = exhibitionDetailModelResponseMessage.getData();
                KPlayCarApp kPlayCarApp = (KPlayCarApp) mView.getContext().getApplicationContext();
                kPlayCarApp.mExhibitin = exhibitionDetailModel.exhibition;
                if (exhibitionDetailModel != null) {
                    if (exhibitionDetailModel.isJump == 0) {
                        mView.showHead(exhibitionDetailModel.exhibition);
                        mView.showMenu(exhibitionDetailModel.menuList);
                        mView.showPlaceList(exhibitionDetailModel.exhibitPlacelist);
                        if (exhibitionDetailModel.coverlist != null && exhibitionDetailModel.coverlist.size() > 2) {
                            mView.showPlink(exhibitionDetailModel.coverlist, 1);
                        } else if (exhibitionDetailModel.coverlist != null && exhibitionDetailModel.pinkImgs.size() > 2) {
                            mView.showPlink(exhibitionDetailModel.pinkImgs, 2);
                        } else {
                            mView.showPlink(exhibitionDetailModel.pinkImgs, 0);
                        }
                    } else {
                        mView.showJump();
                    }

                }
            }
        });
    }


    /**
     * 发帖标签需要展会信息
     *
     * @param exhibitId
     * @param serverCityId
     * @param adcode
     */
    public void loadExhibitionDetail(int exhibitId, String serverCityId, String adcode) {


        RXUtil.execute(mService.getExhibitionDetail(exhibitId, serverCityId, adcode), new Observer<ResponseMessage<ExhibitionDetailModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showLoadingTasksError();
            }

            @Override
            public void onNext(ResponseMessage<ExhibitionDetailModel> exhibitionDetailModelResponseMessage) {
                ExhibitionDetailModel exhibitionDetailModel = exhibitionDetailModelResponseMessage.getData();
                KPlayCarApp kPlayCarApp = (KPlayCarApp) mView.getContext().getApplicationContext();
                kPlayCarApp.mExhibitin = exhibitionDetailModel.exhibition;

                if (exhibitionDetailModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                    mView.showHead(exhibitionDetailModel.exhibition);
                }
            }
        });
    }


    public void getExhibitBanner(int eventId) {
        RXUtil.execute(mService.getExhibitShowBanner(eventId, 1), new Observer<ResponseMessage<ExhibitSeeBannerModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<ExhibitSeeBannerModel> imageResponseMessage) {
                mView.showBanner(imageResponseMessage.getData().imgelist);
            }
        });
    }

    public void getExhibitionList(String areaId, String page, String pagecount, String token, String adcode) {
        RXUtil.execute(mService.getExhibitionList("22", page, pagecount, token, adcode), new Observer<ResponseMessage<ExhibitionListModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.showLoadingTasksError();
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<ExhibitionListModel> exhibitionListModelResponseMessage) {
                ExhibitionListModel exhibitionListModeL = exhibitionListModelResponseMessage.data;

                mView.showExhibitionList(exhibitionListModeL.exhibitionList);
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });

    }


    public void getExhibitionList(Map<String, String> params) {
        RXUtil.execute(mService.getExhibitionList(params), new Observer<ResponseMessage<ExhibitionListModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.showLoadingTasksError();
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<ExhibitionListModel> exhibitionListModelResponseMessage) {
                ExhibitionListModel exhibitionListModeL = exhibitionListModelResponseMessage.data;

                if (Constants.NetworkStatusCode.SUCCESS == exhibitionListModelResponseMessage.statusCode) {
                    mView.showExhibitionList(exhibitionListModeL.exhibitionList);
                } else {

                    CommonUtils.showToast(mView.getContext(), exhibitionListModelResponseMessage.statusMessage);
                }

            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });

    }


    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

    }
}
