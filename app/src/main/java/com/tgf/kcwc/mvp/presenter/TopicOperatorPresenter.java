package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.VehicleService;
import com.tgf.kcwc.mvp.view.TopicOperatorView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;
import rx.Subscription;

/**
 * Author：Jenny
 * Date:2016/12/22 16:55
 * E-mail：fishloveqin@gmail.com
 * 帖子操作Presenter
 */

public class TopicOperatorPresenter extends WrapPresenter<TopicOperatorView> {
    private TopicOperatorView mView;
    private Subscription mSubscription;
    private VehicleService mService = null;

    public void attachView(TopicOperatorView view) {
        this.mView = view;
        mService = ServiceFactory.getVehicleService();
    }


    /**
     * 查询是否已举报
     *
     * @params
     */
    public void isExistReport(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.isExistReport(params),
                new Observer<ResponseMessage<DataItem>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseMessage<DataItem> repData) {

                        if (Constants.NetworkStatusCode.SUCCESS == repData.statusCode) {
                            mView.showData(repData.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), repData.statusMessage + "");
                        }

                    }
                });
        mSubscriptions.add(mSubscription);
    }


    /**
     * 查询是否已举报
     *
     * @params
     */
    public void addReport(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.addReport(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseMessage<Object> repData) {

                        if (Constants.NetworkStatusCode.SUCCESS == repData.statusCode) {
                            mView.showData(repData.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), repData.statusMessage + "");
                        }

                    }
                });
        mSubscriptions.add(mSubscription);
    }

}
