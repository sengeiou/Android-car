package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.PlayHomeService;
import com.tgf.kcwc.mvp.model.PlayListBean;
import com.tgf.kcwc.mvp.model.PlayTopicBean;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.PlayDataView;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class PlayActivityPresenter extends WrapPresenter<PlayDataView> {

    private PlayDataView mView;
    private PlayHomeService mService;

    @Override
    public void attachView(PlayDataView view) {
        mView = view;
        mService = ServiceFactory.getPlayHomeService();
    }

    public void getBannerData(String token) {
        RXUtil.execute(mService.getDrivingBanner(token, "get", "CAR_THREAD_INDEX"),
                new Observer<BannerModel>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dataBannerDefeated("轮播图请求失败");
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(BannerModel bannerModel) {
                        if (bannerModel.code == 0) {
                            mView.dataBannerSucceed(bannerModel);
                        } else {
                            mView.dataBannerDefeated(bannerModel.msg);
                        }

                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                });

    }

    public void getDataLists(String token, String type, String check, int page) {
        RXUtil.execute(mService.getDataLists(token, type, check, page),
                new Observer<PlayListBean>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dataBannerDefeated("列表请求失败");
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(PlayListBean playListBean) {
                        if (playListBean.code == 0) {
                            mView.dataListfeated(playListBean);
                        } else {
                            mView.dataBannerDefeated(playListBean.msg);
                        }
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                });
    }

    public void getShowTopic() {
        RXUtil.execute(mService.getShowTopic(),
                new Observer<ResponseMessage<List<PlayTopicBean>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dataBannerDefeated("列表请求失败");
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(ResponseMessage<List<PlayTopicBean>> playListBean) {
                        if (playListBean.statusCode == 0) {
                            mView.dataTopicSucceed(playListBean.data);
                        } else {
                            mView.dataBannerDefeated(playListBean.statusMessage);
                        }

                    }
                });
    }


}
