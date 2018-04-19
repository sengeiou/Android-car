package com.tgf.kcwc.me.setting;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.AboutModel;
import com.tgf.kcwc.mvp.presenter.AboutPresenter;
import com.tgf.kcwc.mvp.view.AboutView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.WebviewUtil;
import com.tgf.kcwc.view.FunctionView;

/**
 * Author:Jenny
 * Date:2017/10/13
 * E-mail:fishloveqin@gmail.com
 * 关注列表
 */
public class AgreementActivity extends BaseActivity implements AboutView {
    WebView content;
    TextView title;
    //    WebView webView;
    AboutPresenter mPresenter;

    @Override
    protected void setUpViews() {
        title = (TextView) findViewById(R.id.title);
        content = (WebView) findViewById(R.id.content);
        WebSettings settings = content.getSettings();
        content.setWebChromeClient(new WebChromeClient());
        content.setWebViewClient(new WebViewClient());
//        webView = (WebView) findViewById(R.id.webView);
        Log.e("tag", Constants.BaseApi.API_BASE_URL + "agreement/agreement/detail?place=about_us&platform=1&token=" + IOUtils.getToken(this));
//        webView.loadUrl("https://m.51kcwc.com/#/member/user/about");
//        webView.loadData(WebviewUtil.getHtmlData("https://m.51kcwc.com/#/member/user/about"), "text/html; charset=utf-8",
//                "utf-8");

        mPresenter = new AboutPresenter();
        mPresenter.attachView(this);
        mPresenter.getAgreementInfo(IOUtils.getToken(this), 1);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("用户协议");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
    }

    @Override
    public void showAbout(AboutModel model) {

        content.getSettings().setDefaultTextEncodingName("UTF-8");
//        WebviewUtil.getHtmlData()
        //URLUtil.loadWebData(content, WebviewUtil.getHtmlData(model.content));
        content.loadDataWithBaseURL("", WebviewUtil.getHtmlData(model.content), "text/html", "UTF-8", "");
        title.setText(model.name);
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
