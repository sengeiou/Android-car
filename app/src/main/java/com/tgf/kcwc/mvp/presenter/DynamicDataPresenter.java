package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.DynamicDetailsBean;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.VehicleService;
import com.tgf.kcwc.mvp.view.DynamicDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/22 16:55
 * E-mail：fishloveqin@gmail.com
 * 帖子Presenter
 */

public class DynamicDataPresenter extends WrapPresenter<DynamicDataView> {
    private DynamicDataView mView;
    private Subscription mSubscription;
    private VehicleService mService = null;

    public void attachView(DynamicDataView view) {
        this.mView = view;
        mService = ServiceFactory.getVehicleService();
    }

    /**
     * 动态详情
     *
     * @params
     */
    public void detailMood(String token, String thread_id) {

        mSubscription = RXUtil.execute(mService.detailMood(token, thread_id),
                new Observer<ResponseMessage<DynamicDetailsBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(ResponseMessage<DynamicDetailsBean> repData) {
                        if (Constants.NetworkStatusCode.SUCCESS == repData.statusCode) {
                            mView.showData(repData.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), repData.statusMessage + "");
                        }

                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                });
        mSubscriptions.add(mSubscription);
    }


    /**
     * 关注
     *
     * @param token
     * @param follow_id
     */
    public void getFollowAdd(String token, String follow_id ) {
        Subscription subscription = RXUtil.execute(mService.getFollow(token, follow_id), new Observer<BaseBean>() {
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
                    mView.showFollowAddSuccess(baseBean);
                } else {
                    mView.detailsDataFeated(baseBean.msg);
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

    /**
     * 取消关注
     *
     * @param token
     * @param follow_id
     */
    public void getFollowCancel(String token, String follow_id) {
        Subscription subscription = RXUtil.execute(mService.getFellowCancel(token, follow_id), new Observer<BaseBean>() {
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
                    mView.showCancelSuccess(baseBean);
                } else {
                    mView.detailsDataFeated(baseBean.msg);
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
