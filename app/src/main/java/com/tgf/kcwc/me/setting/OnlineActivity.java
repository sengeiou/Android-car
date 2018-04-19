package com.tgf.kcwc.me.setting;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.mvp.presenter.OnlineSetPresenter;
import com.tgf.kcwc.mvp.view.OnLineSetView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.SettingSelectedLayoutView;

/**
 * Auther: Scott
 * Date: 2017/9/5 0005
 * E-mail:hekescott@qq.com
 */

public class OnlineActivity extends BaseActivity implements OnLineSetView {

    private SettingSelectedLayoutView couponSetdLayoutView;
    private OnlineSetPresenter onlineSetPresenter;

    @Override
    protected void setUpViews() {
        couponSetdLayoutView = (SettingSelectedLayoutView) findViewById(R.id.onlineSet_couponlv);
        onlineSetPresenter = new OnlineSetPresenter();
        onlineSetPresenter.attachView(this);
        onlineSetPresenter.getCouponOnline(IOUtils.getToken(getContext()));
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("在线设置");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlineset);
        couponSetdLayoutView.setOnChangelistener(new SettingSelectedLayoutView.OnChangerLisener() {
            @Override
            public void onChange() {
                onlineSetPresenter.setCouponOnline(IOUtils.getToken(getContext()));
            }
        });
    }

    @Override
    public void showSalerSetSuccess() {

    }

    @Override
    public void showSalerSetFailed() {
        couponSetdLayoutView.setStatus(!couponSetdLayoutView.getStauts());
    }

    @Override
    public void showsIsOnline(boolean isOnline) {
        couponSetdLayoutView.setStatus(isOnline);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
