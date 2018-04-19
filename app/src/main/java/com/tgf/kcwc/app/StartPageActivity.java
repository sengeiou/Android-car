package com.tgf.kcwc.app;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.UpdateModel;
import com.tgf.kcwc.mvp.presenter.StartPagePresenter;
import com.tgf.kcwc.mvp.view.UpdateView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Auther: Scott
 * Date: 2017/6/6 0006
 * E-mail:hekescott@qq.com
 */

public class StartPageActivity extends Activity implements UpdateView, View.OnClickListener {

    private ImageView startIv;
    private StartPagePresenter startPagePresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        SharedPreferenceUtil.putUpdateUrl(this,"");
        startPagePresenter = new StartPagePresenter();
        startPagePresenter.attachView(this);

        PackageManager pm  = getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        startPagePresenter.getUpdateApi(pi.versionCode+"");
        startIv = (ImageView) findViewById(R.id.startiv);
        startIv.setOnClickListener(this);
        init();
        clearCache();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
        startIv.startAnimation(animation);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

//                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask,2*1000);
    }

    private void init() {

    }

    private void clearCache() {
        SharedPreferenceUtil.putBrandlist(getContext(), "");
        SharedPreferenceUtil.putExhibitId(getContext(),0);
    }

    public Context getContext() {
        return StartPageActivity.this;
    }

    @Override
    public void showUpdate(UpdateModel update) {
        SharedPreferenceUtil.putUpdateUrl(this, update.download_url);
        SharedPreferenceUtil.putUpdateVer(this, update.version_name);
    }

    @Override
    public void showNoUpdate() {
        SharedPreferenceUtil.putUpdateUrl(this,"");
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startPagePresenter.detachView();
    }

    @Override
    public void onClick(View v) {
        CommonUtils.startNewActivity(StartPageActivity.this, MainActivity.class);
    }
}
