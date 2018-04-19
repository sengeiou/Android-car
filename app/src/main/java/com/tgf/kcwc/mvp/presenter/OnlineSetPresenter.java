package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.SetttingService;
import com.tgf.kcwc.mvp.view.OnLineSetView;
import com.tgf.kcwc.util.RXUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/9/5 0005
 * E-mail:hekescott@qq.com
 */

public class OnlineSetPresenter extends WrapPresenter<OnLineSetView> {
    private OnLineSetView mView;
    private SetttingService mSevice;

    @Override
    public void attachView(OnLineSetView view) {
        mView = view;
        mSevice = ServiceFactory.getSetttingService();
    }

    public void setCouponOnline(String token) {
        RXUtil.execute(mSevice.setCouponOnline(token), new Observer<ResponseMessage>() {
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
                if (responseMessage.statusCode == 0) {
                    mView.showSalerSetSuccess();
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void getCouponOnline(String token) {
        RXUtil.execute(mSevice.getCouponOnline(token), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage jsonObjectResponseMessage) {
                Map map = (Map) jsonObjectResponseMessage.getData();
                int online = (int) map.get("coupon_online");
                mView.showsIsOnline(online == 1);

            }


        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

}
