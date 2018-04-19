package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.SignInService;
import com.tgf.kcwc.mvp.view.SignInSucceedView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;

/**
 * Created by Administrator on 2017/4/21.
 */

public class SignInSucceedPresenter extends WrapPresenter<SignInSucceedView> {

    private SignInSucceedView mView;
    private SignInService mService;

    @Override
    public void attachView(SignInSucceedView view) {
        mView = view;
        mService = ServiceFactory.getSignInService();
    }

    public void gainAppLsis(String token, String address, String lat, String lng, String rideCheckId) {
        RXUtil.execute(mService.getCheckNow(token, address, lat, lng, rideCheckId), new Observer<BaseBean>() {
            @Override
            public void onCompleted() {
                mView.getContext();
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(BaseBean appListBean) {
                if (appListBean.code==0){
                    mView.dataListSucceed(appListBean);
                }else {
                    mView.dataListDefeated(appListBean.msg);
                }
            }
        });
    }

}
