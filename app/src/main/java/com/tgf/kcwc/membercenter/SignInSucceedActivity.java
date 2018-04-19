package com.tgf.kcwc.membercenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.MainActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.driv.DrivingActivity;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.presenter.SignInSucceedPresenter;
import com.tgf.kcwc.mvp.view.SignInSucceedView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

/**
 * Created by Administrator on 2017/6/5.
 */

public class SignInSucceedActivity extends BaseActivity implements SignInSucceedView {
    protected String ID = "";
    protected SignInSucceedPresenter mSignInSucceedPresenter;
    protected  KPlayCarApp mKPlayCarApp;
    protected  TextView title;
    protected  TextView backhome;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("签到");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mKPlayCarApp = (KPlayCarApp) getApplication();
        ID = getIntent().getStringExtra(Constants.IntentParams.ID);
        setContentView(R.layout.activity_signinsucceed);
        title = (TextView) findViewById(R.id.title);
        backhome = (TextView) findViewById(R.id.backhome);
        mSignInSucceedPresenter = new SignInSucceedPresenter();
        mSignInSucceedPresenter.attachView(this);
        mSignInSucceedPresenter.gainAppLsis(IOUtils.getToken(mContext), mKPlayCarApp.getAddressInfo(), mKPlayCarApp.getLattitude(), mKPlayCarApp.getLongitude(), ID);
        title.setVisibility(View.GONE);
        backhome.setVisibility(View.GONE);
    }

    @Override
    public void dataListSucceed(BaseBean appListBean) {
        title.setVisibility(View.VISIBLE);
        backhome.setVisibility(View.VISIBLE);
        CommonUtils.showToast(mContext, "成功");
        title.setText("当前到达：" + mKPlayCarApp.getAddressInfo());
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
        title.setVisibility(View.VISIBLE);
        title.setText(msg);
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
        return mContext;
    }
}
