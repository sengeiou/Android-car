package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ExhibitionArryBean;
import com.tgf.kcwc.mvp.model.StoreExhibitionBean;
import com.tgf.kcwc.mvp.model.StoreExhibitionService;
import com.tgf.kcwc.mvp.model.StreBelowAmendBean;
import com.tgf.kcwc.mvp.view.StoreExhibitionView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/4/21.
 */

public class StreExhibitionPresenter extends WrapPresenter<StoreExhibitionView> {

    private StoreExhibitionView mView;
    private StoreExhibitionService mService;

    @Override
    public void attachView(StoreExhibitionView view) {
        mView = view;
        mService = ServiceFactory.getStoreExhibitionService();
    }

    public void getDataLists(String token, int page, String status, String event_id) {
        RXUtil.execute(mService.getDataLists(token, page, status, event_id),
                new Observer<StoreExhibitionBean>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dataListDefeated("网络异常，稍候再试！");
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(StoreExhibitionBean storeBelowBean) {
                        if (storeBelowBean.code == 0) {
                            mView.dataSucceed(storeBelowBean);
                        } else if (storeBelowBean.code == 10023) {
                            mView.showLoadingTasksError();
                            CommonUtils.showToast(mView.getContext(), storeBelowBean.msg);
                        } else {
                            CommonUtils.showToast(mView.getContext(), storeBelowBean.msg);
                        }
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                });
    }

    public void getEventList(String token) {
        RXUtil.execute(mService.getEventList(token), new Observer<ExhibitionArryBean>() {
            @Override
            public void onCompleted() {
                // mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ExhibitionArryBean exhibitionArryBean) {
                if (exhibitionArryBean.code == 0) {
                    mView.eventListSucceed(exhibitionArryBean);
                } else {
                    CommonUtils.showToast(mView.getContext(), exhibitionArryBean.msg);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                // mView.setLoadingIndicator(true);
            }
        });
    }

    public void getCancel(String token, String id, final int num) {
        RXUtil.execute(mService.getCancel(token, id), new Observer<StreBelowAmendBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(StreBelowAmendBean baseBean) {
                if (baseBean.code == 0) {
                    mView.revocationSucceed(baseBean, num);
                } else {
                    CommonUtils.showToast(mView.getContext(), baseBean.msg);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void getRelease(String token, String id, final int num) {
        RXUtil.execute(mService.getCancel(token, id), new Observer<StreBelowAmendBean>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(StreBelowAmendBean baseBean) {
                if (baseBean.code == 0) {
                    mView.releaseSucceed(baseBean, num);
                } else {
                    CommonUtils.showToast(mView.getContext(), baseBean.msg);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

}
