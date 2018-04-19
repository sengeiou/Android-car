package com.tgf.kcwc.ticket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.AboutTicketModel;
import com.tgf.kcwc.mvp.presenter.TicketDataPresenter;
import com.tgf.kcwc.mvp.view.TicketDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.WebviewUtil;
import com.tgf.kcwc.view.FunctionView;

/**
 * Author：Jenny
 * Date:2017/1/3 20:44
 * E-mail：fishloveqin@gmail.com
 */

public class AboutTicketActivity extends BaseActivity implements TicketDataView<AboutTicketModel> {

    private int mId;
    private TicketDataPresenter mPresenter;
    private WebView mWebView;
    private String mName = "";
    private TextView mNameTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        mName = intent.getStringExtra(Constants.IntentParams.DATA);
        setContentView(R.layout.activity_about_ticket);
        mPresenter = new TicketDataPresenter();
        mPresenter.attachView(this);
        mPresenter.getAboutTicket(mId + "", IOUtils.getToken(mContext));
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText(R.string.about_ticket);
    }

    @Override
    protected void setUpViews() {


        mWebView = (WebView) findViewById(R.id.webView);
        mNameTv = (TextView) findViewById(R.id.senseName);
        findViewById(R.id.titleLayout).setOnClickListener(this);
        mNameTv.setText(mName);
    }


    @Override
    public void onClick(View view) {

        SharedPreferenceUtil.putExhibitId(getContext(),mId);
        KPlayCarApp.putValue(Constants.IntentParams.INDEX, 1);
//        Intent intent=new Intent();
//        intent.putExtra(Constants.IntentParams.INDEX, 1);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setClass(mContext, MainActivity.class);
//        startActivity(intent);

        CommonUtils.gotoMainPage(mContext,Constants.Navigation.SEE_INDEX);
    }

    @Override
    public void showData(AboutTicketModel aboutTicketModel) {

        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.loadDataWithBaseURL("", WebviewUtil.getHtmlData(aboutTicketModel.information), "text/html", "UTF-8", "");
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        if (active) {
            showLoadingDialog();
        } else {
            dismissLoadingDialog();
        }
    }

    @Override
    public void showLoadingTasksError() {
        dismissLoadingDialog();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
