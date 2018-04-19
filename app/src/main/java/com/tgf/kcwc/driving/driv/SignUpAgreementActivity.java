package com.tgf.kcwc.driving.driv;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.AgreementModel;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.presenter.SignUpPresenter;
import com.tgf.kcwc.mvp.view.SingUpView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.WebviewUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.List;

/**
 * Created by Administrator on 2017/10/10.
 */

public class SignUpAgreementActivity extends BaseActivity implements SingUpView {

    private SignUpPresenter mSignUpPresenter;
    private WebView webView;
    private String type = "";
    TextView title;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("报名协议");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinupagreement);
        type = (String) getIntent().getSerializableExtra(Constants.IntentParams.ID);
        webView = (WebView) findViewById(R.id.web_view);
        title = (TextView) findViewById(R.id.title_bar_text);
        ;
        mSignUpPresenter = new SignUpPresenter();
        mSignUpPresenter.attachView(this);
        if (!TextUtil.isEmpty(type)) {
            if (type.equals("1")) { //开车去报名协议
                title.setText("报名协议");
                mSignUpPresenter.gainAppLsis("sign", "1");
            } else if (type.equals("2")) {//请你玩报名协议
                title.setText("报名协议");
                mSignUpPresenter.gainAppLsis("sign_2", "1");
            } else if (type.equals("3")) {//开车去活动协议
                title.setText("活动协议");
                mSignUpPresenter.gainAppLsis("drive", "1");
            } else if (type.equals("4")) {//请你玩活动协议
                title.setText("活动协议");
                mSignUpPresenter.gainAppLsis("play", "1");
            }
        }
    }

    @Override
    public void dataListSucceed(BaseBean attentionBean) {

    }

    @Override
    public void dataSucceed(List<AgreementModel> attentionBean) {
        webView.loadData(WebviewUtil.getHtmlData(attentionBean.get(0).content), "text/html; charset=utf-8",
                "utf-8");
    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {
        CommonUtils.showToast(mContext, "系统异常");
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
