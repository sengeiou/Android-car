package com.tgf.kcwc.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.model.CityBean;
import com.tgf.kcwc.mvp.presenter.CityPresenter;
import com.tgf.kcwc.mvp.view.CityView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.view.MyCitySelectView;

import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */

public class ErgodicCityService extends Service {

    CityPresenter mCityPresenter;
    Context context;
    KPlayCarApp mKPlayCarApp ;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mKPlayCarApp = (KPlayCarApp) getApplication();
        context = getApplicationContext();
        mCityPresenter = new CityPresenter();
        mCityPresenter.attachView(new CityView() {
            @Override
            public void dataListSucceed(CityBean cityBean) {
                List<CityBean.Select> datalist = cityBean.data.list;
                Ergodic(datalist);
            }

            @Override
            public void dataListDefeated(String msg) {

            }

            @Override
            public void setLoadingIndicator(boolean active) {

            }

            @Override
            public void showLoadingTasksError() {

            }

            @Override
            public Context getContext() {
                return context;
            }
        });

        String Locationdata = SharedPreferenceUtil.getLocation(context);
        String data = SharedPreferenceUtil.getCitylist(context);
        Gson gson = new Gson();
        if (TextUtils.isEmpty(data)) {
            mCityPresenter.gainAppLsis(IOUtils.getToken(context));
        } else {
            if (TextUtils.isEmpty(Locationdata)) {
                CityBean cityBean = gson.fromJson(data, CityBean.class);
                Ergodic(cityBean.data.list);
            } else {
                Brand LocationBean = gson.fromJson(Locationdata, Brand.class);
                if (LocationBean.brandName.equals(mKPlayCarApp.locCityName)) {
                    Log.i("TT", "noon");
                    stopSelf();
                } else {
                    if (comparisonTime()){
                        mCityPresenter.gainAppLsis(IOUtils.getToken(context));
                    }else {
                        CityBean cityBean = gson.fromJson(data, CityBean.class);
                        Ergodic(cityBean.data.list);
                    }

                }
            }

        }
    }

    public void Ergodic(List<CityBean.Select> datalist) {

        for (CityBean.Select data : datalist) {

            if (data.name.equals(mKPlayCarApp.locCityName)) {
                Brand brand = new Brand();
                brand.id = data.id;
                brand.pid = data.pid;
                brand.brandName = data.name;
                brand.letter = data.namecode;
                brand.setBaseIndexTag(brand.letter);
                Gson gson = new Gson();
                String introJson = gson.toJson(brand);
                SharedPreferenceUtil.putLocation(context, introJson);
                break;
            }
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("TT", "onDestroy");
    }

    public boolean comparisonTime() {
        long cityTime = SharedPreferenceUtil.getCityTime(context);
        if (cityTime==0){
            return true;
        }
        long ltime = MyCitySelectView.gaiPresentTime();
        long diff = ltime - cityTime;
        if (diff > 0) {
            if (diff <= (1 * 24 * 60 * 60*1000)) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

}
