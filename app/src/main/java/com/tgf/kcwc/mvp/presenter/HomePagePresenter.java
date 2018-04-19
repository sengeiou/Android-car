package com.tgf.kcwc.mvp.presenter;

import android.util.Log;

import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.ExhibitionService;
import com.tgf.kcwc.mvp.model.HomeModel;
import com.tgf.kcwc.mvp.model.MotoOrder;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.HomePageView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/5/15 0015
 * E-mail:hekescott@qq.com
 */

public class HomePagePresenter extends WrapPresenter<HomePageView> {


    private HomePageView mView;
    private ExhibitionService mService;

    @Override
    public void attachView(HomePageView view) {
        mView = view;
        mService = ServiceFactory.getExhibitionService();
    }

    public void getHomePageView(String mark) {
        Subscription subscription=   RXUtil.execute(mService.getBanner("get", mark), new Observer<BannerModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BannerModel bannerModel) {
                mView.showBannerView(bannerModel.data);
            }
        });
        mSubscriptions.add(subscription);
    }

    public void getHomeList(String token, String type, int cityId, String lng, String lat, int page, int pageSize) {
        RXUtil.execute(mService.getHomeList(token, type, cityId, lng, lat, page, pageSize), new Observer<ResponseMessage<HomeModel>>() {

            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<HomeModel> homeModelResponseMessage) {
                mView.showHomeList(homeModelResponseMessage.getData().homeList);
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

//    public void commitMotoOrder(String token, int carId, int carSeriesId,String contact, String lat, String log, String tel) {
//        Subscription subscription=   RXUtil.execute(
//                mService.commitMotoOrder(token, carId, carSeriesId,contact, lat, log, tel),
//                new Observer<ResponseMessage<MotoOrder>>() {
//                    @Override
//                    public void onCompleted() {
//                        mView.setLoadingIndicator(false);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
////                        mView.showCommitOrderFailed(e.getLocalizedMessage());
//                        mView.setLoadingIndicator(false);
//                    }
//
//                    @Override
//                    public void onNext(ResponseMessage<MotoOrder> motoOrderResponseMessage) {
//                        Log.e("TAG", "onNext: "+motoOrderResponseMessage.statusMessage);
//                        if (motoOrderResponseMessage.statusCode == 0) {
//                            mView.showCommitOrderSuccess(motoOrderResponseMessage.getData().OrderId);
//                        } else {
//                            mView.showCommitOrderFailed(motoOrderResponseMessage.statusMessage);
//                        }
//                    }
//
//                }, new Action0() {
//                    @Override
//                    public void call() {
//                        mView.setLoadingIndicator(true);
//                    }
//                });
//        mSubscriptions.add(subscription);
//    }

    public void getFollowAdd(String token, String follow_id,final int position) {
       Subscription subscription= RXUtil.execute(mService.getFollow(token, follow_id), new Observer<BaseBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                Logger.d("getFollowAdd==  baseBean");
                if (baseBean.code == 0) {
                    mView.showFollowAddSuccess(position);
                    Logger.d("getFollowAdd==  code");
                } else {
                    mView.showFollowAddFailed(baseBean.msg,position);
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
    public void getFollowCancel(String token, String follow_id,final int position){
        Subscription subscription= RXUtil.execute(mService.getFellowCancel(token, follow_id), new Observer<BaseBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                if (baseBean.code == 0) {
                    mView.showCancelSuccess(position);
                } else {
                    mView.showFollowAddFailed(baseBean.msg,position);
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
    public void executePraise(String token,String type, String resourceId,final int position){
        Subscription subscription= RXUtil.execute(mService.executePraise(resourceId, type, token), new Observer<ResponseMessage<Object>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<Object> objectResponseMessage) {
                if(objectResponseMessage.statusCode==0){
                    mView.showPraiseSuccess(position);
                }else {
                    mView.showPraiseFailed(objectResponseMessage.statusMessage);
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
    public void deleteThread(String token,String threadId,final int position){
        Subscription subscription= RXUtil.execute(mService.deleteThread(threadId,token), new Observer<ResponseMessage<Object>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<Object> objectResponseMessage) {
                if(objectResponseMessage.statusCode==0){
                    mView.showDeletSucess(position);
                }else {
                    mView.showPraiseFailed(objectResponseMessage.statusMessage);
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
