package com.tgf.kcwc.mvp.presenter;

import com.google.gson.Gson;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.CityBean;
import com.tgf.kcwc.mvp.model.CityService;
import com.tgf.kcwc.mvp.view.CityView;
import com.tgf.kcwc.util.RXUtil;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.view.MyCitySelectView;

import rx.Observer;

/**
 * Created by Administrator on 2017/4/21.
 */

public class CityPresenter extends WrapPresenter<CityView> {

    private CityView mView;
    private CityService mService;

    @Override
    public void attachView(CityView view) {
        mView=view;
        mService= ServiceFactory.getCityService();
    }

    public void gainAppLsis(String token){
        RXUtil.execute(mService.getCityList(token), new Observer<CityBean>() {
            @Override
            public void onCompleted() {
                mView.getContext();
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
            }

            @Override
            public void onNext(CityBean cityBean) {
                if (cityBean.code==0){
                    long l = MyCitySelectView.gaiPresentTime();
                    Gson gson = new Gson();
                    SharedPreferenceUtil.putCitylist(mView.getContext(),gson.toJson(cityBean));
                    SharedPreferenceUtil.putCityTime(mView.getContext(),l);
                    mView.dataListSucceed(cityBean);
                }else {
                    mView.dataListDefeated(cityBean.msg);
                }
            }
        });
    }



}
