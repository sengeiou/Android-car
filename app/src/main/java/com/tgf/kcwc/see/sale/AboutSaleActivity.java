package com.tgf.kcwc.see.sale;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.mvp.model.AboutModel;
import com.tgf.kcwc.mvp.presenter.AboutPresenter;
import com.tgf.kcwc.mvp.view.AboutView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.WebviewUtil;
import com.tgf.kcwc.view.FunctionView;

/**
 * Author：Jenny
 * Date:2017/1/3 20:44
 * E-mail：fishloveqin@gmail.com
 * 车主自售协议
 */

public class AboutSaleActivity extends BaseActivity implements AboutView{
    WebView content;
    TextView title;
    AboutPresenter mPresenter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("关于车主自售");
    }

    @Override
    protected void setUpViews() {
        title = (TextView) findViewById(R.id.title);
        content = (WebView) findViewById(R.id.content);
        WebSettings settings = content.getSettings();
        content.setWebChromeClient(new WebChromeClient());
        content.setWebViewClient(new WebViewClient());

        mPresenter = new AboutPresenter();
        mPresenter.attachView(this);
        mPresenter.getAbout(IOUtils.getToken(this),1,"sale");
    }
    @Override
    public void showAbout(AboutModel model) {
        content.getSettings().setDefaultTextEncodingName("UTF-8");
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
