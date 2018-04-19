package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.GeneralizationService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.RideDataModel;
import com.tgf.kcwc.mvp.model.TripBookDetailModel;
import com.tgf.kcwc.mvp.model.TripBookService;
import com.tgf.kcwc.mvp.view.TripBookDetailView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/5/11 0011
 * E-mail:hekescott@qq.com
 */

public class TirpBookDetailPresenter extends WrapPresenter<TripBookDetailView> {
    private TripBookDetailView mView;
    private TripBookService mService;
    private GeneralizationService mGeneralService;

    @Override
    public void attachView(TripBookDetailView view) {
        mView = view;
        mService = ServiceFactory.getTripBookService();
        mGeneralService = ServiceFactory.getGeneralizationService();
    }

    public void getTripbookDetail(String bookId, String token) {
       Subscription subscription =  RXUtil.execute(mService.getTripBookDetail(bookId, token),
                new Observer<ResponseMessage<TripBookDetailModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseMessage<TripBookDetailModel> tripBookDetailModelResponseMessage) {
                        TripBookDetailModel tripBookDetailModel = tripBookDetailModelResponseMessage
                                .getData();
                        mView.showTitle(tripBookDetailModel);
                        mView.showUserInfo(tripBookDetailModel.userInfo);
                        mView.showRideReport(tripBookDetailModel.rideReport);
                        mView.showRoadLineDesc(tripBookDetailModel.lineList);
                        mView.showRecomdInfo(tripBookDetailModel.recommendInfo);
                        mView.showTagList(tripBookDetailModel.topiclist);
                        mView.showHonorList(tripBookDetailModel.honorList);
                    }
                });
        mSubscriptions.add(subscription);
    }

    public void getRoadLinelist(String token) {
        Subscription subscription =  RXUtil.execute(mService.getRideLines(token), new Observer<ResponseMessage<RideDataModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<RideDataModel> rideDataModelResponseMessage) {
                int rideId = rideDataModelResponseMessage.data.ride_id;
                if (rideId != 0) {
                    if (rideDataModelResponseMessage.data.status == 0) {//骑行中
                        mView.showUnStop(rideDataModelResponseMessage.getData());
                    }
                }else {
                    mView.setLoadingIndicator(false);
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

    public void deleteTripBook(int bookId, String token) {
        RXUtil.execute(mService.deleterTripBook(bookId,token), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                mView.showDeleteSuccess();
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

}
