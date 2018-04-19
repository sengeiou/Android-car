package com.tgf.kcwc.certificate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.CertResultModel;
import com.tgf.kcwc.mvp.presenter.CertDataPresenter;
import com.tgf.kcwc.mvp.view.CertDataView;
import com.tgf.kcwc.view.FunctionView;

/**
 * Author：Jenny
 * Date:2017/1/3 20:44
 * E-mail：fishloveqin@gmail.com
 * 证件使用须知
 */

public class AboutCertActivity extends BaseActivity implements CertDataView<CertResultModel> {

    protected TextView title;
    protected TextView desc;
    private String mId;
    private String name = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mId = intent.getStringExtra(Constants.IntentParams.ID);
        name = intent.getStringExtra(Constants.IntentParams.DATA);
        super.setContentView(R.layout.activity_about_cert);
        initView();
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("使用须知");
    }

    private WebView mWebView;
    private CertDataPresenter mPresenter;

    @Override
    protected void setUpViews() {

        initView();
        mPresenter = new CertDataPresenter();
        mPresenter.attachView(this);
        mPresenter.loadCertDesc(mId + "");
        title.setText(name+"");
    }

    @Override
    public void showData(CertResultModel certResultModel) {

        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.loadDataWithBaseURL("", certResultModel.detail.rules, "text/html", "UTF-8", "");
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {
        dismissLoadingDialog();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.webView);
        title = (TextView) findViewById(R.id.title);
        desc = (TextView) findViewById(R.id.desc);
    }
}
